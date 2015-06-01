package com.yeahmobi.yscheduler.condition;

import com.yeahmobi.yscheduler.model.type.DependingStatus;
import com.yeahmobi.yscheduler.model.type.TaskInstanceStatus;
import com.yeahmobi.yscheduler.model.type.WorkflowInstanceStatus;

public abstract class SelfDependencyCondition implements Condition {

    public boolean satisfy(ConditionContext context) {
        DependingStatus dependingStatus = getDependingStatus(context);
        if ((dependingStatus == null) || (dependingStatus == DependingStatus.NONE)) {
            return true;
        } else {
            Object realStatus = getLastStatus(context);
            if (realStatus == null) {
                return true;
            } else if (realStatus instanceof TaskInstanceStatus) {
                TaskInstanceStatus taskInstanceStatus = (TaskInstanceStatus) realStatus;
                if ((dependingStatus == DependingStatus.SUCCESS) && (taskInstanceStatus == TaskInstanceStatus.SUCCESS)) {
                    return true;
                } else if ((dependingStatus == DependingStatus.COMPLETED) && (taskInstanceStatus.isCompleted())) {
                    return true;
                }
            } else if (realStatus instanceof WorkflowInstanceStatus) {
                WorkflowInstanceStatus workflowInstanceStatus = (WorkflowInstanceStatus) realStatus;
                if ((dependingStatus == DependingStatus.SUCCESS)
                    && (workflowInstanceStatus == WorkflowInstanceStatus.SUCCESS)) {
                    return true;
                } else if ((dependingStatus == DependingStatus.COMPLETED) && (workflowInstanceStatus.isCompleted())) {
                    return true;
                }
            }
        }
        return false;
    }

    protected abstract DependingStatus getDependingStatus(ConditionContext context);

    protected abstract Object getLastStatus(ConditionContext context);

}
