package com.yeahmobi.yscheduler.agentframework.agent.event.spring;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import com.yeahmobi.yscheduler.agentframework.agent.event.BaseEventMapper;
import com.yeahmobi.yscheduler.agentframework.agent.event.EventHandler;

/**
 * @author Leo.Liang
 */
public class SpringEventMapper extends BaseEventMapper implements InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(SpringEventMapper.class);

    public void afterPropertiesSet() throws Exception {
        for (Map.Entry<String, EventHandler> entry : this.handlers.entrySet()) {
            log.info("Register one event handler. Key={}, class={}", entry.getKey(),
                     entry.getValue().getClass().getName());
        }
    }
}
