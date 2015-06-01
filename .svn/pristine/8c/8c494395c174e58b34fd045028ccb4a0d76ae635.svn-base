package com.yeahmobi.yscheduler.condition;

import org.springframework.beans.factory.annotation.Autowired;

import com.yeahmobi.yscheduler.model.Task;
import com.yeahmobi.yscheduler.model.Workflow;
import com.yeahmobi.yscheduler.model.WorkflowInstance;
import com.yeahmobi.yscheduler.model.service.WorkflowInstanceService;
import com.yeahmobi.yscheduler.model.type.DependingStatus;

public class WorkflowSelfDependencyCondition extends SelfDependencyCondition {

    @Autowired
    private WorkflowInstanceService workflowInstanceService;

    @Override
    protected DependingStatus getDependingStatus(ConditionContext context) {
        Task task = context.getTask();
        Workflow workflow = context.getWorkflow();
        if ((task != null) || (workflow == null)) {
            return null;
        } else {
            return workflow.getLastStatusDependency();
        }
    }

    @Override
    protected Object getLastStatus(ConditionContext context) {
        Task task = context.getTask();
        Workflow workflow = context.getWorkflow();
        WorkflowInstance workflowInstance = context.getWorkflowInstance();
        if (task != null) {
            return null;
        } else {
            WorkflowInstance lastWorkflowInstance = this.workflowInstanceService.getLast(workflow, workflowInstance);
            return lastWorkflowInstance == null ? null : lastWorkflowInstance.getStatus();
        }
    }
}
