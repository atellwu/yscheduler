package com.yeahmobi.yscheduler.web.controller.workflow.common;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeahmobi.yscheduler.common.CrontabUtils;
import com.yeahmobi.yscheduler.model.Task;
import com.yeahmobi.yscheduler.model.User;
import com.yeahmobi.yscheduler.model.Workflow;
import com.yeahmobi.yscheduler.model.WorkflowDetail;
import com.yeahmobi.yscheduler.model.common.UserContextHolder;
import com.yeahmobi.yscheduler.model.service.TaskService;
import com.yeahmobi.yscheduler.model.service.UserService;
import com.yeahmobi.yscheduler.model.service.WorkflowDetailService;
import com.yeahmobi.yscheduler.model.service.WorkflowService;
import com.yeahmobi.yscheduler.model.type.DependingStatus;
import com.yeahmobi.yscheduler.model.type.WorkflowStatus;
import com.yeahmobi.yscheduler.web.vo.WorkflowDetailVO;

/**
 * @author Ryan Sun
 */
@Service
public class CommonWorkflowHelper {

    private static final String   TASK_ERROR_MSG = "请检查任务的填写";

    @Autowired
    private WorkflowDetailService workflowDetailService;

    @Autowired
    private WorkflowService       workflowService;

    @Autowired
    private TaskService           taskService;

    @Autowired
    private UserService           userService;

    @SuppressWarnings("unchecked")
    public void saveWorkflowDetail(List<WorkflowDetailVO> detailVos, Workflow workflow, long userId) {
        List<WorkflowDetail> details = new ArrayList<WorkflowDetail>(CollectionUtils.collect(detailVos,
                                                                                             new Transformer() {

                                                                                                 public Object transform(Object input) {
                                                                                                     return ((WorkflowDetailVO) input).getWorkflowDetail();
                                                                                                 }
                                                                                             }));
        List<List<Long>> dependencyList = new ArrayList<List<Long>>(CollectionUtils.collect(detailVos,
                                                                                            new Transformer() {

                                                                                                public Object transform(Object input) {
                                                                                                    return ((WorkflowDetailVO) input).getDependencies();
                                                                                                }
                                                                                            }));
        this.workflowDetailService.save(workflow.getId(), details, dependencyList, userId);
    }

    @SuppressWarnings("unchecked")
    public void saveWorkflowDetail(List<WorkflowDetailVO> detailVos, Workflow workflow) {
        List<WorkflowDetail> details = new ArrayList<WorkflowDetail>(CollectionUtils.collect(detailVos,
                                                                                             new Transformer() {

                                                                                                 public Object transform(Object input) {
                                                                                                     return ((WorkflowDetailVO) input).getWorkflowDetail();
                                                                                                 }
                                                                                             }));
        List<List<Long>> dependencyList = new ArrayList<List<Long>>(CollectionUtils.collect(detailVos,
                                                                                            new Transformer() {

                                                                                                public Object transform(Object input) {
                                                                                                    return ((WorkflowDetailVO) input).getDependencies();
                                                                                                }
                                                                                            }));
        this.workflowDetailService.save(workflow.getId(), details, dependencyList);
    }

    private Workflow buildAndSaveWorkflow(Integer timeout, String description, String crontab, Workflow workflow) {
        workflow.setCrontab(crontab);
        workflow.setDescription(description);
        workflow.setOwner(UserContextHolder.getUserContext().getId());
        workflow.setTimeout(timeout);
        workflow.setCanSkip(false);
        workflow.setLastStatusDependency(DependingStatus.NONE);
        this.workflowService.createOrUpdate(workflow);
        return workflow;
    }

    public Workflow createWorkflow(String name, Integer timeout, String description, String crontab, boolean running) {
        Workflow workflow = new Workflow();
        workflow.setName(name);
        workflow.setCommon(true);
        if (running) {
            workflow.setStatus(WorkflowStatus.OPEN);
        } else {
            workflow.setStatus(WorkflowStatus.PAUSED);
        }
        return buildAndSaveWorkflow(timeout, description, crontab, workflow);
    }

    public Workflow updateWorkflow(Long id, Integer timeout, String description, String crontab) {
        Workflow workflow = new Workflow();
        workflow.setId(id);
        return buildAndSaveWorkflow(timeout, description, crontab, workflow);
    }

    public void validate(String name, Integer timeout, String crontab) {
        // name
        Validate.isTrue(StringUtils.isNotBlank(name), "工作流名称不能为空");
        Validate.isTrue(!this.workflowService.nameExist(name), "工作流名称重复");
        validate(timeout, crontab);
    }

    public void validate(Integer timeout, String crontab) {
        // timeout
        Validate.isTrue((timeout != null) && (timeout >= 0), "超时时间不合法");
        // crontab
        Validate.isTrue(StringUtils.isNotBlank(crontab), "调度表达式为空");
        crontab = CrontabUtils.normalize(crontab, false);
    }

    public List<WorkflowDetailVO> parse(long workflowId, HttpServletRequest request) {
        List<WorkflowDetailVO> detailVos = new ArrayList<WorkflowDetailVO>();
        long userId = UserContextHolder.getUserContext().getId();
        Task teamRootTask = this.taskService.getRootTask(this.userService.getTeamName(userId));
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            String strTaskId = request.getParameter("taskId_" + i);
            String strTaskTimeout = request.getParameter("taskTimeout_" + i);
            String strTaskRetrycount = request.getParameter("taskRetrycount_" + i);
            String strTaskDelay = request.getParameter("taskDelay_" + i);
            String strTaskCondition = request.getParameter("taskCondition_" + i);

            String[] strDependencyTaskIds = request.getParameterValues("dependencyTaskId_" + i);
            // 4项都为空，则停止
            if ((strTaskId == null) && (strTaskTimeout == null) && (strTaskRetrycount == null)
                && (strTaskDelay == null) && ((strDependencyTaskIds == null) || (strDependencyTaskIds.length == 0))) {
                break;
            }

            WorkflowDetailVO detailVo = new WorkflowDetailVO();
            detailVos.add(detailVo);

            // 验证
            Validate.isTrue(StringUtils.isNotBlank(strTaskId), "任务必选");
            Validate.isTrue(StringUtils.isNotBlank(strTaskCondition), "任务自依赖条件必填");

            WorkflowDetail detail = new WorkflowDetail();
            List<Long> dependencies = new ArrayList<Long>();
            detailVo.setWorkflowDetail(detail);
            detailVo.setDependencies(dependencies);
            try {
                // 暂未使用
                detail.setRetryTimes(0);
                detail.setTimeout(60);

                Long taskId = Long.valueOf(strTaskId);
                Integer taskDelay = null;
                if (StringUtils.isNotBlank(strTaskDelay)) {
                    taskDelay = Integer.valueOf(strTaskDelay);
                }
                detail.setTaskId(taskId);
                detail.setDelay(taskDelay);
                if (DependingStatus.COMPLETED.name().equalsIgnoreCase(strTaskCondition)) {
                    detail.setLastStatusDependency(DependingStatus.COMPLETED);
                } else if (DependingStatus.SUCCESS.name().equalsIgnoreCase(strTaskCondition)) {
                    detail.setLastStatusDependency(DependingStatus.SUCCESS);
                } else {
                    detail.setLastStatusDependency(DependingStatus.NONE);
                }
                Task task = this.taskService.get(taskId);
                Long teamId = this.userService.get(task.getOwner()).getTeamId();
                detailVo.setTaskName(task.getName());
                detailVo.setTeamId(teamId);

            } catch (RuntimeException e) {
                throw new IllegalArgumentException(TASK_ERROR_MSG, e);
            }

            if (strDependencyTaskIds != null) {
                for (String strDependencyTaskId : strDependencyTaskIds) {
                    Long dependencyTaskId = Long.valueOf(strDependencyTaskId);
                    dependencies.add(dependencyTaskId);
                }
            }
            if (!this.taskService.isRootTask(detail.getTaskId())) {
                addTeamRootIfNeed(dependencies, teamRootTask.getId(), userId);
            }

        }
        List<WorkflowDetailVO> detailVosToValidate = new ArrayList<WorkflowDetailVO>(detailVos);

        if (!UserContextHolder.getUserContext().isAdmin()) {
            List<WorkflowDetail> allDetails = this.workflowDetailService.list(workflowId);
            List<WorkflowDetail> detailsOfSameTeam = this.workflowDetailService.list(workflowId, userId);
            @SuppressWarnings("unchecked")
            List<WorkflowDetail> detailsOfOtherTeam = new ArrayList<WorkflowDetail>(
                                                                                    CollectionUtils.subtract(allDetails,
                                                                                                             detailsOfSameTeam));
            for (WorkflowDetail detail : detailsOfOtherTeam) {
                WorkflowDetailVO vo = new WorkflowDetailVO();
                Long taskId = detail.getTaskId();
                vo.setWorkflowDetail(detail);
                vo.setTaskName(this.taskService.get(taskId).getName());
                vo.setDependencies(this.workflowDetailService.listDependencyTaskIds(workflowId, taskId));
                vo.setNeedValidate(false);
                detailVosToValidate.add(vo);
            }
        }

        CommonWorkflowDependencyValidate validator = new CommonWorkflowDependencyValidate(detailVosToValidate,
                                                                                          this.taskService,
                                                                                          this.userService);

        validator.validate();

        return detailVos;
    }

    private void addTeamRootIfNeed(List<Long> dependencies, long teamRootTaskId, long userId) {
        if (UserContextHolder.getUserContext().isAdmin()) {
            return;
        }
        User currentUser = this.userService.get(userId);
        for (Long dependency : dependencies) {
            User owner = this.userService.get(this.taskService.get(dependency).getOwner());
            if (currentUser.getTeamId() == owner.getTeamId()) {
                return;
            }
        }
        dependencies.add(teamRootTaskId);
    }
}
