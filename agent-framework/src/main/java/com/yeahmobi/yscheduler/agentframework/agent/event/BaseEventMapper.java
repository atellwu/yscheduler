package com.yeahmobi.yscheduler.agentframework.agent.event;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Leo.Liang
 */
public class BaseEventMapper implements EventMapper {

    protected Map<String, EventHandler> handlers = new HashMap<String, EventHandler>();

    public void setHandlers(Map<String, EventHandler> handlers) {
        this.handlers = handlers;
    }

    public EventHandler findHandler(String eventType) {
        return this.handlers.get(eventType);
    }

    public void add(String eventType, EventHandler eventHandler) {
        this.handlers.put(eventType, eventHandler);
    }

}
