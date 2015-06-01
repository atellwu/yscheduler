package com.yeahmobi.yscheduler.workflow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.yeahmobi.yscheduler.condition.ConditionChecker;
import com.yeahmobi.yscheduler.condition.ConditionContext;
import com.yeahmobi.yscheduler.executor.TaskInstanceExecutor;
import com.yeahmobi.yscheduler.model.Task;
import com.yeahmobi.yscheduler.model.TaskInstance;
import com.yeahmobi.yscheduler.model.Workflow;
import com.yeahmobi.yscheduler.model.WorkflowInstance;
import com.yeahmobi.yscheduler.model.service.TaskInstanceService;
import com.yeahmobi.yscheduler.model.service.TaskService;
import com.yeahmobi.yscheduler.model.service.UserService;
import com.yeahmobi.yscheduler.model.service.WorkflowDetailService;
import com.yeahmobi.yscheduler.model.service.WorkflowInstanceService;
import com.yeahmobi.yscheduler.model.service.WorkflowService;
import com.yeahmobi.yscheduler.model.type.TaskInstanceStatus;
import com.yeahmobi.yscheduler.model.type.WorkflowInstanceStatus;
import com.yeahmobi.yscheduler.notice.NoticeService;

/**
 * @author Leo Liang
 */
public abstract class AbstractWorkflowEngine implements WorkflowEngine {

    private static final Logger           LOGGER                         = LoggerFactory.getLogger(AbstractWorkflowEngine.class);

    protected Map<Long, Pair>             runningWorkflows               = new ConcurrentHashMap<Long, Pair>();
    @Autowired
    protected TaskInstanceExecutor        taskInstanceExecutor;
    @Autowired
    protected WorkflowInstanceService     workflowInstanceService;
    @Autowired
    protected WorkflowService             workflowService;
    @Autowired
    protected TaskInstanceService         taskInstanceService;
    @Autowired
    protected TaskService                 taskService;
    @Autowired
    protected UserService                 userService;
    @Autowired
    protected WorkflowDetailService       workflowDetailService;
    @Autowired
    protected NoticeService               noticeService;
    @Autowired
    protected ConditionChecker            conditionChecker;

    protected Thread                      worker;
    protected AtomicBoolean               closed                         = new AtomicBoolean(false);
    protected ConcurrentSkipListSet<Long> cancelledWorkflowInstanceIdSet = new ConcurrentSkipListSet<Long>();
    protected ConcurrentSkipListSet<Long> timeoutWorkflowInstanceIdSet   = new ConcurrentSkipListSet<Long>();

    // abstract method start
    protected abstract List<WorkflowInstance> getAllRunningWorkflowInstances();

    protected abstract String getName();

    protected abstract long getCheckIntervalMilliseconds();

    protected abstract void checkAndExecute(Entry<Long, Pair> runningWorkflowEntry);

    // abstract method end

    @PostConstruct
    public void init() {
        List<WorkflowInstance> workflowInstances = getAllRunningWorkflowInstances();
        for (WorkflowInstance workflowInstance : workflowInstances) {
            Long workflowInstanceId = workflowInstance.getId();
            Map<Long, TaskInstanceWithDependency> dependenciesMapping = buildWorkflowAndDependencies(workflowInstance.getWorkflowId(),
                                                                                                     workflowInstanceId,
                                                                                                     this.taskInstanceService.listByWorkflowInstanceId(workflowInstanceId));
            submitToWorker(this.workflowService.get(workflowInstance.getWorkflowId()), workflowInstanceId,
                           dependenciesMapping);
        }
        this.worker = new Thread(new Runnable() {

            public void run() {
                while (!AbstractWorkflowEngine.this.closed.get()) {
                    for (Map.Entry<Long, Pair> entry : AbstractWorkflowEngine.this.runningWorkflows.entrySet()) {
                        try {
                            checkAndExecute(entry);
                        } catch (Exception e) {
                            LOGGER.error(e.getMessage(), e);
                        }
                    }
                    try {
                        TimeUnit.MILLISECONDS.sleep(getCheckIntervalMilliseconds());
                    } catch (InterruptedException e) {
                        // ignore
                    }
                }
            }

        });
        this.worker.setName(getName());
        this.worker.setDaemon(true);
        this.worker.start();
    }

    @PreDestroy
    public void close() {
        if (this.closed.compareAndSet(false, true)) {
            this.worker.interrupt();
        }
    }

    protected Map<Long, TaskInstanceWithDependency> buildWorkflowAndDependencies(Long workflowId,
                                                                                 Long workflowInstanceId,
                                                                                 List<TaskInstance> taskInstances) {
        Map<Long, TaskInstanceWithDependency> dependenciesMapping = new HashMap<Long, TaskInstanceWithDependency>();
        Map<Long, TaskInstance> taskIdToInstanceMap = new HashMap<Long, TaskInstance>();
        for (TaskInstance taskInstance : taskInstances) {
            taskIdToInstanceMap.put(taskInstance.getTaskId(), taskInstance);
        }

        for (TaskInstance taskInstance : taskInstances) {
            List<Long> dependencyTaskIds = this.workflowDetailService.listDependencyTaskIds(workflowId,
                                                                                            taskInstance.getTaskId());
            List<Long> dependencyTaskInstanceIds = new ArrayList<Long>();
            for (Long id : dependencyTaskIds) {
                dependencyTaskInstanceIds.add(taskIdToInstanceMap.get(id).getId());
            }
            Task task = this.taskService.get(taskInstance.getTaskId());
            Long teamId = this.userService.get(task.getOwner()).getTeamId();
            TaskInstanceWithDependency taskInstanceWd = new TaskInstanceWithDependency(task, teamId, taskInstance,
                                                                                       dependencyTaskInstanceIds);
            dependenciesMapping.put(taskInstance.getId(), taskInstanceWd);
        }

        return dependenciesMapping;
    }

    protected void submitToWorker(Workflow workflow, Long workflowInstanceId,
                                  Map<Long, TaskInstanceWithDependency> dependenciesMapping) {
        this.runningWorkflows.put(workflowInstanceId, new Pair(workflow, dependenciesMapping));
    }

    public void submit(Workflow workflow, WorkflowInstance workflowInstance, List<TaskInstance> taskIntances) {
        if (this.runningWorkflows.containsKey(workflowInstance)) {
            throw new IllegalArgumentException("重复提交工作流");
        }
        Long workflowInstanceId = workflowInstance.getId();
        updateWorkflowInstanceStatus(workflowInstanceId, WorkflowInstanceStatus.RUNNING);
        Map<Long, TaskInstanceWithDependency> dependenciesMapping = buildWorkflowAndDependencies(workflowInstance.getWorkflowId(),
                                                                                                 workflowInstanceId,
                                                                                                 taskIntances);
        submitToWorker(workflow, workflowInstanceId, dependenciesMapping);
    }

    protected List<TaskInstanceWithDependency> fetchLatestStatus(Collection<TaskInstanceWithDependency> taskInstanceWithDependencies) {
        List<TaskInstanceWithDependency> taskInstancesStatusChanged = new ArrayList<TaskInstanceWithDependency>();
        for (TaskInstanceWithDependency taskInstanceWd : taskInstanceWithDependencies) {
            TaskInstance taskInstance = taskInstanceWd.getTaskInstance();
            if (taskInstance.getStatus() != TaskInstanceStatus.DEPENDENCY_WAIT) {
                TaskInstanceStatus status = this.taskInstanceExecutor.getStatus(taskInstance.getId());

                // 记录状态发生变化的taskInstance
                if (status != taskInstance.getStatus()) {
                    taskInstancesStatusChanged.add(taskInstanceWd);
                }

                taskInstance.setStatus(status);
            }
        }
        return taskInstancesStatusChanged;
    }

    protected boolean hasFailTask(Long workflowInstanceId,
                                  Collection<TaskInstanceWithDependency> taskInstanceWithDependencies) {
        for (TaskInstanceWithDependency taskInstanceWd : taskInstanceWithDependencies) {
            TaskInstance taskInstance = taskInstanceWd.getTaskInstance();
            TaskInstanceStatus status = taskInstance.getStatus();
            if ((status == TaskInstanceStatus.FAILED) || (status == TaskInstanceStatus.COMPLETE_WITH_UNKNOWN_STATUS)) {
                return true;
            }
        }

        return false;
    }

    protected boolean isAllTasksCompleted(Long workflowInstanceId,
                                          Collection<TaskInstanceWithDependency> taskInstanceWithDependencies) {
        for (TaskInstanceWithDependency taskInstanceWd : taskInstanceWithDependencies) {
            TaskInstance taskInstance = taskInstanceWd.getTaskInstance();
            if (!taskInstance.getStatus().isCompleted()) {
                return false;
            }
        }

        return true;
    }

    protected boolean isAllTasksSuccess(Long workflowInstanceId,
                                        Collection<TaskInstanceWithDependency> taskInstanceWithDependencies) {
        for (TaskInstanceWithDependency taskInstanceWd : taskInstanceWithDependencies) {
            TaskInstance taskInstance = taskInstanceWd.getTaskInstance();
            if (taskInstance.getStatus() != TaskInstanceStatus.SUCCESS) {
                return false;
            }
        }

        return true;
    }

    protected void workflowFail(Long workflowInstanceId,
                                Collection<TaskInstanceWithDependency> taskInstanceWithDependencies) {
        updateWorkflowInstanceStatus(workflowInstanceId, WorkflowInstanceStatus.FAILED);
        this.noticeService.workflowFail(workflowInstanceId);
        clearRunningEntry(workflowInstanceId);
    }

    protected void workflowSuccess(Long workflowInstanceId) {
        updateWorkflowInstanceStatus(workflowInstanceId, WorkflowInstanceStatus.SUCCESS);

        if (this.timeoutWorkflowInstanceIdSet.contains(workflowInstanceId)) {
            notifySuccess(workflowInstanceId);
        }

        clearRunningEntry(workflowInstanceId);
    }

    protected void workflowCancel(Long workflowInstanceId) {
        updateWorkflowInstanceStatus(workflowInstanceId, WorkflowInstanceStatus.CANCELLED);

        clearRunningEntry(workflowInstanceId);
    }

    private void clearRunningEntry(Long workflowInstanceId) {
        this.runningWorkflows.remove(workflowInstanceId);

        this.timeoutWorkflowInstanceIdSet.remove(workflowInstanceId);
        clearCancelledIdSet();
    }

    protected boolean hasCancelledTaskAndNoUncompleted(Long workflowInstanceId,
                                                       Collection<TaskInstanceWithDependency> taskInstanceWithDependencies) {
        boolean hasCancelled = false;
        if (this.cancelledWorkflowInstanceIdSet.contains(workflowInstanceId)) {
            for (TaskInstanceWithDependency taskInstanceWd : taskInstanceWithDependencies) {
                TaskInstance taskInstance = taskInstanceWd.getTaskInstance();
                if (!taskInstance.getStatus().isCompleted()) {
                    return false;
                } else if (TaskInstanceStatus.CANCELLED.equals(taskInstance.getStatus())) {
                    hasCancelled = true;
                }
            }

            return hasCancelled;
        }
        return false;
    }

    protected boolean handleCancelIfNeeded(Long workflowInstanceId,
                                           Collection<TaskInstanceWithDependency> taskInstanceWithDependencies) {
        if (this.cancelledWorkflowInstanceIdSet.contains(workflowInstanceId)) {
            for (TaskInstanceWithDependency taskInstanceWd : taskInstanceWithDependencies) {
                TaskInstance taskInstance = taskInstanceWd.getTaskInstance();
                if (TaskInstanceStatus.DEPENDENCY_WAIT.equals(taskInstance.getStatus())) {
                    taskInstance.setStatus(TaskInstanceStatus.CANCELLED);
                    this.taskInstanceService.updateStatus(taskInstance.getId(), TaskInstanceStatus.CANCELLED);
                } else {
                    this.taskInstanceExecutor.cancel(taskInstance.getId());
                }
            }

            return true;
        }
        return false;
    }

    protected void cancelAllRunningTasks(Long workflowInstanceId,
                                         Collection<TaskInstanceWithDependency> taskInstanceWithDependencies) {
        for (TaskInstanceWithDependency taskInstanceWd : taskInstanceWithDependencies) {
            TaskInstance taskInstance = taskInstanceWd.getTaskInstance();
            if (taskInstance.getStatus() == TaskInstanceStatus.RUNNING) {
                this.taskInstanceExecutor.cancel(taskInstance.getId());
            }
        }

    }

    protected void setAllDependencyWaitTasksAsWorkflowFail(Long workflowInstanceId,
                                                           Collection<TaskInstanceWithDependency> taskInstanceWithDependencies) {
        for (TaskInstanceWithDependency taskInstanceWd : taskInstanceWithDependencies) {
            TaskInstance taskInstance = taskInstanceWd.getTaskInstance();
            TaskInstanceStatus status = taskInstance.getStatus();
            if (status == TaskInstanceStatus.DEPENDENCY_WAIT) {
                taskInstance.setStatus(TaskInstanceStatus.WORKFLOW_FAILED);
                this.taskInstanceService.updateStatus(taskInstance.getId(), TaskInstanceStatus.WORKFLOW_FAILED);
            }
        }

    }

    private void updateWorkflowInstanceStatus(Long workflowInstanceId, WorkflowInstanceStatus status) {
        this.workflowInstanceService.updateStatus(workflowInstanceId, status);
    }

    protected void clearCancelledIdSet() {
        Iterator<Long> iterator = this.cancelledWorkflowInstanceIdSet.iterator();
        while (iterator.hasNext()) {
            Long workflowInstanceId = iterator.next();
            if (!this.runningWorkflows.containsKey(workflowInstanceId)) {
                iterator.remove();
            }
        }
    }

    protected void notifySuccess(Long workflowInstanceId) {
        this.noticeService.workflowSuccess(workflowInstanceId);
    }

    protected boolean checkTimeout(Workflow workflow, WorkflowInstance workflowInstance) {
        Long workflowInstanceId = workflowInstance.getId();
        if (this.timeoutWorkflowInstanceIdSet.contains(workflowInstanceId)) {
            // 如果已经超时，说明已经检查过，不再检查
            return true;
        }
        long timeout = workflow.getTimeout() * 60 * 1000L;
        Date startTime = workflowInstance.getScheduleTime();
        if (startTime == null) {
            startTime = workflowInstance.getStartTime();
        }
        if (startTime != null) {
            long duration = System.currentTimeMillis() - startTime.getTime();
            if (duration > timeout) {
                // 报警
                this.timeoutWorkflowInstanceIdSet.add(workflowInstanceId);
                this.noticeService.workflowTimeout(workflowInstanceId);
                return true;
            }
        }
        return false;
    }

    public void cancel(long workflowInstanceId) {
        this.cancelledWorkflowInstanceIdSet.add(workflowInstanceId);
    }

    protected void submitToTaskInstanceExecutorIfConditionSatisfy(Workflow workflow,
                                                                  WorkflowInstance workflowInstance,
                                                                  Collection<TaskInstanceWithDependency> taskInstanceWithDependencies,
                                                                  TaskInstanceWithDependency taskInstanceWd) {
        TaskInstance taskInstance = taskInstanceWd.getTaskInstance();

        List<TaskInstance> dependencyTaskInstances = new ArrayList<TaskInstance>();

        for (TaskInstanceWithDependency wd : taskInstanceWithDependencies) {
            if (taskInstanceWd.getDependencies().contains(wd.getTaskInstance().getId())) {
                dependencyTaskInstances.add(wd.getTaskInstance());
            }
        }

        Long taskId = taskInstance.getTaskId();
        ConditionContext context = new ConditionContext(
                                                        workflow,
                                                        workflowInstance,
                                                        this.workflowDetailService.get(workflowInstance.getWorkflowId(),
                                                                                       taskId),
                                                        this.taskService.get(taskId), taskInstance,
                                                        dependencyTaskInstances);
        if ((taskInstance.getStatus() == TaskInstanceStatus.DEPENDENCY_WAIT) && this.conditionChecker.satisfy(context)) {
            taskInstance.setStatus(TaskInstanceStatus.READY);
            this.taskInstanceService.updateStatus(taskInstance.getId(), TaskInstanceStatus.READY);
            this.taskInstanceExecutor.submit(taskInstance);
        }
    }

    protected static class Pair {

        Workflow                              workflow;
        Map<Long, TaskInstanceWithDependency> taskInstances;

        public Pair(Workflow workflow, Map<Long, TaskInstanceWithDependency> taskInstances) {
            this.workflow = workflow;
            this.taskInstances = taskInstances;
        }
    }
}
