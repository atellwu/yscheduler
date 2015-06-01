package com.yeahmobi.yscheduler.common.variable;

public interface VariableHandler {

    public String process(String[] params, VariableContext context) throws VariableException;
}
