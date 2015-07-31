package com.yeahmobi.yscheduler.agentframework.meta;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

public class AgentMeta {

    public static final String AGENT_HOME;
    public static final String AGENT_VERSION;

    static {
        AGENT_HOME = System.getProperty("agent.home", "unknown");
        try {
            AGENT_VERSION = StringUtils.trim(IOUtils.toString(AgentMeta.class.getResourceAsStream("/.version")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
