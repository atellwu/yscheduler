package com.yeahmobi.yscheduler.condition;

import com.yeahmobi.yscheduler.model.TaskInstance;
import com.yeahmobi.yscheduler.model.type.TaskInstanceStatus;

public class DependencyCondition implements Condition {

    public boolean satisfy(ConditionContext context) {
        if (context.getDependencyTaskInstances() != null) {
            for (TaskInstance taskInstance : context.getDependencyTaskInstances()) {
                if (taskInstance.getStatus() != TaskInstanceStatus.SUCCESS) {
                    return false;
                }
            }
            return true;
        }
        return true;
    }

}
