/**
 *
 */
package com.yeahmobi.yscheduler.agentframework.agent.event;

/**
 * @author Leo.Liang
 */
public interface EventMapper {

    public EventHandler findHandler(String eventType);

    public void add(String eventType, EventHandler eventHandler);
}
