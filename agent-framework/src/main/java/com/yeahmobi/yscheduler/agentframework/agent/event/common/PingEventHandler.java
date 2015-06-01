package com.yeahmobi.yscheduler.agentframework.agent.event.common;

import java.util.Map;

import com.yeahmobi.yscheduler.agentframework.agent.event.EventHandler;
import com.yeahmobi.yscheduler.agentframework.agent.event.HandlerResult;

/**
 * @author Leo.Liang
 */
public class PingEventHandler implements EventHandler {

    public static final String EVENT_TYPE = "PING";

    public void onEvent(Map<String, String> params, HandlerResult handlerResult) {
        handlerResult.setSuccess(true);
    }

}
