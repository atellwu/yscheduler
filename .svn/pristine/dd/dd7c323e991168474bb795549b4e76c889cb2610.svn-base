package com.yeahmobi.yscheduler.condition;

import java.util.Date;

public class DelayCondition implements Condition {

    public boolean satisfy(ConditionContext context) {
        if ((context.getWorkflowInstance() == null) || (context.getWorkflowDetail() == null)) {
            return true;
        }
        Date scheduleTime = context.getWorkflowInstance().getScheduleTime();
        if ((scheduleTime == null) || (context.getWorkflowDetail().getDelay() == null)) {
            return true;
        }
        Date time = new Date();
        if (time.getTime() < (scheduleTime.getTime() + (context.getWorkflowDetail().getDelay() * 60 * 1000))) {
            return false;
        } else {
            return true;
        }

    }

}
