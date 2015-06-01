package com.yeahmobi.yscheduler.scheduler;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.yeahmobi.yscheduler.common.CrontabUtils;
import com.yeahmobi.yscheduler.condition.ConditionChecker;
import com.yeahmobi.yscheduler.condition.ConditionContext;
import com.yeahmobi.yscheduler.executor.TaskInstanceExecutor;
import com.yeahmobi.yscheduler.model.Task;
import com.yeahmobi.yscheduler.model.TaskInstance;
import com.yeahmobi.yscheduler.model.Workflow;
import com.yeahmobi.yscheduler.model.WorkflowInstance;
import com.yeahmobi.yscheduler.model.service.ScheduleProgressService;
import com.yeahmobi.yscheduler.model.service.TaskInstanceService;
import com.yeahmobi.yscheduler.model.service.TaskService;
import com.yeahmobi.yscheduler.model.service.WorkflowInstanceService;
import com.yeahmobi.yscheduler.model.service.WorkflowService;
import com.yeahmobi.yscheduler.model.type.TaskInstanceStatus;
import com.yeahmobi.yscheduler.model.type.TaskStatus;
import com.yeahmobi.yscheduler.model.type.WorkflowInstanceStatus;
import com.yeahmobi.yscheduler.model.type.WorkflowStatus;
import com.yeahmobi.yscheduler.notice.NoticeService;
import com.yeahmobi.yscheduler.workflow.WorkflowExecutor;

@Service
public class DefaultSchedulerExecutor implements SchedulerExecutor {

    private static final Logger     LOGGER                     = LoggerFactory.getLogger(DefaultSchedulerExecutor.class);

    private static final long       INTERVAL                   = 10 * 1000;

    private static final int        MYSQL_ERROR_CODE_DUPLICATE = 1062;

    private AtomicBoolean           closed                     = new AtomicBoolean(false);

    private ScheduleThread          controllerThread;

    private long                    currentScheduleTime;

    @Autowired
    private ScheduleProgressService scheduleProgressService;

    @Autowired
    private WorkflowExecutor        workflowExecutor;

    @Autowired
    private WorkflowService         workflowService;

    @Autowired
    private WorkflowInstanceService workflowInstanceService;

    @Autowired
    private TaskInstanceExecutor    taskInstanceExecutor;

    @Autowired
    private TaskService             taskService;

    @Autowired
    private TaskInstanceService     taskInstanceService;

    @Autowired
    private NoticeService           noticeService;

    @Autowired
    private ConditionChecker        conditionChecker;

    @PostConstruct
    public void init() {
        boolean recover = Boolean.parseBoolean(System.getProperty("ysheduler.schedule.recover", "true"));

        if (recover) {
            Long scheduleTime = this.scheduleProgressService.getCurrentScheduleTime();
            this.currentScheduleTime = scheduleTime != null ? scheduleTime : System.currentTimeMillis();
            LOGGER.info("Recover from " + this.currentScheduleTime);
        } else {
            this.currentScheduleTime = System.currentTimeMillis();
        }

        // 启动后台执行线程
        this.controllerThread = new ScheduleThread();
        this.controllerThread.setName("scheduler");
        this.controllerThread.setDaemon(true);
        this.controllerThread.start();
    }

    @PreDestroy
    public void close() {
        if (this.closed.compareAndSet(false, true)) {
            this.controllerThread.interrupt();
        }
    }

    private class ScheduleThread extends Thread {

        @Override
        public void run() {

            while (!DefaultSchedulerExecutor.this.closed.get()) {
                try {
                    List<Workflow> workflowList = DefaultSchedulerExecutor.this.workflowService.listAll(WorkflowStatus.OPEN);
                    List<Task> taskList = DefaultSchedulerExecutor.this.taskService.list(TaskStatus.OPEN);
                    // 当前调度时间
                    Date curDate = new Date(DefaultSchedulerExecutor.this.currentScheduleTime);

                    if (workflowList != null) {

                        for (Workflow workflow : workflowList) {
                            Date scheduleTime = CrontabUtils.next(workflow.getCrontab(), workflow.getLastScheduleTime());

                            long diff = curDate.getTime() - scheduleTime.getTime();
                            if (diff >= 0) {
                                // 判断是否存在未完成的workflow（Inited和RUNNING），如有，则不触发调度; 被跳过，也持久化到数据库
                                boolean existUncompleted = DefaultSchedulerExecutor.this.workflowInstanceService.existUncompleted(workflow.getId());

                                WorkflowInstance instance = new WorkflowInstance();
                                instance.setScheduleTime(scheduleTime);
                                instance.setWorkflowId(workflow.getId());
                                if (!existUncompleted || ((workflow.getCanSkip() != null) && !workflow.getCanSkip())) {
                                    ConditionContext context = new ConditionContext(workflow, instance, null, null,
                                                                                    null, null);
                                    if (DefaultSchedulerExecutor.this.conditionChecker.satisfy(context)) {

                                        instance.setStatus(WorkflowInstanceStatus.INITED);

                                        try {
                                            DefaultSchedulerExecutor.this.workflowInstanceService.save(instance);
                                            DefaultSchedulerExecutor.this.workflowExecutor.submit(workflow, instance);
                                            DefaultSchedulerExecutor.this.workflowService.updateScheduleTime(workflow.getId(),
                                                                                                             scheduleTime);

                                        } catch (DuplicateKeyException e) {
                                            // ignored 重复了说明有被调度，调度时间应该更新
                                            DefaultSchedulerExecutor.this.workflowService.updateScheduleTime(workflow.getId(),
                                                                                                             scheduleTime);
                                        } catch (Exception e) {
                                            LOGGER.error(e.getMessage(), e);
                                        }
                                    }
                                } else {
                                    instance.setStatus(WorkflowInstanceStatus.SKIPPED);
                                    try {
                                        DefaultSchedulerExecutor.this.workflowInstanceService.save(instance);
                                        DefaultSchedulerExecutor.this.noticeService.workflowSkip(instance.getId(),
                                                                                                 scheduleTime);
                                        DefaultSchedulerExecutor.this.workflowService.updateScheduleTime(workflow.getId(),
                                                                                                         scheduleTime);
                                    } catch (DuplicateKeyException e) {
                                        // ignored 重复了说明有被调度，调度时间应该更新
                                        DefaultSchedulerExecutor.this.workflowService.updateScheduleTime(workflow.getId(),
                                                                                                         scheduleTime);
                                    } catch (Exception e) {
                                        LOGGER.error(e.getMessage(), e);
                                    }
                                }
                            }
                        }
                    }

                    if (taskList != null) {
                        for (Task task : taskList) {
                            Date scheduleTime = CrontabUtils.next(task.getCrontab(), task.getLastScheduleTime());

                            long diff = curDate.getTime() - scheduleTime.getTime();
                            if (diff >= 0) {
                                // 判断是否存在(相同taskId,存在schedule，状态未完成的)task（Inited和RUNNING），如有，则不触发调度; 被跳过，也持久化到数据库
                                boolean existUncompleted = DefaultSchedulerExecutor.this.taskInstanceService.existUncompletedScheduled(task.getId());

                                TaskInstance instance = new TaskInstance();
                                instance.setTaskId(task.getId());
                                instance.setScheduleTime(scheduleTime);
                                if (!existUncompleted || ((task.getCanSkip() != null) && !task.getCanSkip())) {
                                    ConditionContext context = new ConditionContext(null, null, null, task, instance,
                                                                                    null);
                                    if (DefaultSchedulerExecutor.this.conditionChecker.satisfy(context)) {
                                        instance.setStatus(TaskInstanceStatus.READY);
                                        try {
                                            DefaultSchedulerExecutor.this.taskInstanceService.save(instance);
                                            DefaultSchedulerExecutor.this.taskInstanceExecutor.submit(instance);
                                            DefaultSchedulerExecutor.this.taskService.updateLastScheduleTime(task.getId(),
                                                                                                             scheduleTime);
                                        } catch (DuplicateKeyException e) {
                                            // ignored 重复了说明有被调度，调度时间应该更新
                                            DefaultSchedulerExecutor.this.taskService.updateLastScheduleTime(task.getId(),
                                                                                                             scheduleTime);
                                        } catch (Exception e) {
                                            LOGGER.error(e.getMessage(), e);
                                        }
                                    }
                                } else {
                                    instance.setStatus(TaskInstanceStatus.SKIPPED);
                                    try {
                                        DefaultSchedulerExecutor.this.taskInstanceService.save(instance);
                                        DefaultSchedulerExecutor.this.noticeService.taskSkip(instance.getId(),
                                                                                             scheduleTime);
                                        DefaultSchedulerExecutor.this.taskService.updateLastScheduleTime(task.getId(),
                                                                                                         scheduleTime);
                                    } catch (DuplicateKeyException e) {
                                        // ignored 重复了说明有被调度，调度时间应该更新
                                        DefaultSchedulerExecutor.this.taskService.updateLastScheduleTime(task.getId(),
                                                                                                         scheduleTime);
                                    } catch (DataIntegrityViolationException e) {
                                        if ((e.getCause() instanceof SQLIntegrityConstraintViolationException)
                                            && (((SQLIntegrityConstraintViolationException) e.getCause()).getErrorCode() == MYSQL_ERROR_CODE_DUPLICATE)) {
                                            // ignored 重复了说明有被调度，调度时间应该更新
                                            DefaultSchedulerExecutor.this.taskService.updateLastScheduleTime(task.getId(),
                                                                                                             scheduleTime);
                                        } else {
                                            LOGGER.error(e.getMessage(), e);
                                        }
                                    } catch (Exception e) {
                                        LOGGER.error(e.getMessage(), e);
                                    }
                                }
                            }
                        }
                    }

                    // 每隔 INTERVAL 时间调度一次
                    DefaultSchedulerExecutor.this.currentScheduleTime += INTERVAL;
                    // 持久化进度
                    try {
                        DefaultSchedulerExecutor.this.scheduleProgressService.saveCurrentScheduleTime(DefaultSchedulerExecutor.this.currentScheduleTime);
                    } catch (Exception e) {
                        LOGGER.error("Error when save the currentScheduleTime", e);
                    }

                    try {
                        if (System.currentTimeMillis() < DefaultSchedulerExecutor.this.currentScheduleTime) {
                            parkUntil(DefaultSchedulerExecutor.this.currentScheduleTime);
                        } else {
                            LockSupport.parkNanos(1000);
                        }
                    } catch (InterruptedException e) {
                        // ignored. maybe will close.
                    }
                } catch (Throwable e) {
                    // log for check the code
                    LOGGER.error("No Error should reach here, please check the code", e);
                }
            }
        }
    }

    private void parkUntil(long absTime) throws InterruptedException {
        while (System.currentTimeMillis() < absTime) {
            LockSupport.parkUntil(absTime);
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }
        }
    }

}
