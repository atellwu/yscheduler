package com.yeahmobi.yscheduler.condition;

import org.springframework.beans.factory.annotation.Autowired;

import com.yeahmobi.yscheduler.model.Task;
import com.yeahmobi.yscheduler.model.TaskInstance;
import com.yeahmobi.yscheduler.model.WorkflowDetail;
import com.yeahmobi.yscheduler.model.WorkflowInstance;
import com.yeahmobi.yscheduler.model.service.TaskInstanceService;
import com.yeahmobi.yscheduler.model.service.WorkflowInstanceService;
import com.yeahmobi.yscheduler.model.type.DependingStatus;

public class TaskSelfDependencyCondition extends SelfDependencyCondition {

    @Autowired
    private TaskInstanceService     taskInstanceService;

    @Autowired
    private WorkflowInstanceService workflowInstanceService;

    @Override
    protected DependingStatus getDependingStatus(ConditionContext context) {
        Task task = context.getTask();
        WorkflowDetail wfDetail = context.getWorkflowDetail();
        if (task == null) {
            return null;
        } else if (wfDetail == null) {
            return task.getLastStatusDependency();
        } else {
            return wfDetail.getLastStatusDependency();
        }
    }

    @Override
    protected Object getLastStatus(ConditionContext context) {
        Task task = context.getTask();
        TaskInstance taskInstance = context.getTaskInstance();
        WorkflowDetail wfDetail = context.getWorkflowDetail();
        if (task == null) {
            return null;
        } else if (wfDetail == null) {
            TaskInstance lastTaskInstance = this.taskInstanceService.getLast(task, taskInstance);
            return lastTaskInstance == null ? null : lastTaskInstance.getStatus();
        } else {
            WorkflowInstance lastWorkflowInstance = this.workflowInstanceService.getLast(context.getWorkflow(),
                                                                                         context.getWorkflowInstance());
            if (lastWorkflowInstance == null) {
                return null;
            } else {
                TaskInstance lastTaskInstance = this.taskInstanceService.get(task.getId(), lastWorkflowInstance.getId());
                return lastTaskInstance == null ? null : lastTaskInstance.getStatus();
            }
        }
    }
}
