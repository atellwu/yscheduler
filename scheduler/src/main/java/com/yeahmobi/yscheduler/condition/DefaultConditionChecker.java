package com.yeahmobi.yscheduler.condition;

import java.util.List;

public class DefaultConditionChecker implements ConditionChecker {

    private List<Condition> conditions;

    public void setConditions(List<Condition> conditions) {
        this.conditions = conditions;
    }

    public boolean satisfy(ConditionContext context) {
        for (Condition condition : this.conditions) {
            if (!condition.satisfy(context)) {
                return false;
            }
        }
        return true;
    }
}
