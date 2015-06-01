package com.yeahmobi.yscheduler.agentframework.agent.event.spring;

import javax.servlet.ServletContext;

import org.springframework.web.context.support.WebApplicationContextUtils;

import com.yeahmobi.yscheduler.agentframework.agent.event.EventMapper;
import com.yeahmobi.yscheduler.agentframework.agent.event.EventMapperFactory;

/**
 * @author Leo.Liang
 */
public class SpringEventMapperFactory implements EventMapperFactory {

    public EventMapper getEventMapper(ServletContext servletContext) {
        return WebApplicationContextUtils.getWebApplicationContext(servletContext).getBean(SpringEventMapper.class);
    }

}
