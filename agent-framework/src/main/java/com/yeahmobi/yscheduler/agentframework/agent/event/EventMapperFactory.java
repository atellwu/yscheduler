package com.yeahmobi.yscheduler.agentframework.agent.event;

import javax.servlet.ServletContext;

/**
 * @author Leo.Liang
 */
public interface EventMapperFactory {

    public EventMapper getEventMapper(ServletContext servletContext);
}
