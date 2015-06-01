package com.yeahmobi.yscheduler.agentframework.agent.task.async;

import java.util.Map;

import com.yeahmobi.yscheduler.agentframework.agent.event.task.CalloutTaskExecutor;
import com.yeahmobi.yscheduler.agentframework.agent.task.AbstractAgentTask;

public class CalloutAgentTask extends AbstractAgentTask {

    public CalloutAgentTask(String eventType, Map<String, String> params) {
        super(eventType, params);
    }

    private CalloutTaskExecutor executor = new CalloutTaskExecutor();

    public CalloutTaskExecutor getTaskExecutor() {
        return this.executor;
    }

}
