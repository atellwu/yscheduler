package com.yeahmobi.yscheduler.agentframework.agent.task;

import java.util.Map;

public abstract class AbstractAgentTask implements AgentTask {

    public static final String  PARAM_TASKNAME      = "taskName";

    public static final String  PARAM_ATTMEPT_ID    = "attemptId";

    public static final String  PARAM_AGENT_HOST    = "agentHost";

    public static final String  PARAM_WORKFLOW_NAME = "workflowName";

    private Map<String, String> params;

    private String              taskName;

    private String              eventType;

    private long                attemptId;

    public AbstractAgentTask(String eventType, Map<String, String> params) {
        super();
        this.eventType = eventType;
        this.params = params;
        this.taskName = params.get(PARAM_TASKNAME);
        this.attemptId = Long.parseLong(params.get(PARAM_ATTMEPT_ID));
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

    public long getAttemptId() {
        return this.attemptId;
    }

}
