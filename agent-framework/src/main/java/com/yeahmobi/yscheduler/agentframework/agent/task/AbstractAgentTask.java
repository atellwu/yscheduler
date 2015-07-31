package com.yeahmobi.yscheduler.agentframework.agent.task;

import java.util.Map;

public abstract class AbstractAgentTask implements AgentTask {

    public static final String  PARAM_TASKNAME = "taskName";

    private Map<String, String> params;

    private String              taskName;

    private String              eventType;

    public AbstractAgentTask(String eventType, Map<String, String> params) {
        super();
        this.eventType = eventType;
        this.params = params;
        this.taskName = params.get(PARAM_TASKNAME);
    }

    public Map<String, String> getTaskParams() {
        return this.params;
    }

    public String getEventType() {
        return this.eventType;
    }

    public String getTaskName() {
        return this.taskName;
    }

}
