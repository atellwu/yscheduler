package com.yeahmobi.yscheduler.agentframework.variable;

import com.yeahmobi.yscheduler.agentframework.meta.AgentMeta;
import com.yeahmobi.yscheduler.common.variable.VariableContext;
import com.yeahmobi.yscheduler.common.variable.VariableException;
import com.yeahmobi.yscheduler.common.variable.VariableHandler;

public class AgentVersionHandler implements VariableHandler {

    public static final String VARIABLE_NAME = "agentVersion";

    public String process(String[] params, VariableContext variableContext) throws VariableException {
        return AgentMeta.AGENT_VERSION;
    }
}
