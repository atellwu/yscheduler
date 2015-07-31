package com.yeahmobi.yscheduler.agentframework.agent.event.task;

import java.util.Map;

import com.yeahmobi.yscheduler.agentframework.agent.event.HandlerResult;
import com.yeahmobi.yscheduler.agentframework.agent.event.TaskExecutionEventHandler;

/**
 * @author Leo.Liang
 */
public class TaskStatusEventHandler extends TaskExecutionEventHandler {

    public static final String EVENT_TYPE = "TASK_STATUS";

    public void onEvent(Map<String, String> params, HandlerResult handlerResult) {
        checkStatus(params, handlerResult);
    }
}
