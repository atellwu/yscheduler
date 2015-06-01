package com.yeahmobi.yscheduler.agentframework.client;

import com.yeahmobi.yscheduler.agentframework.AgentRequest;
import com.yeahmobi.yscheduler.agentframework.AgentResponse;
import com.yeahmobi.yscheduler.agentframework.exception.AgentClientException;

public interface AgentClient {

    public <T> AgentResponse<T> call(String host, AgentRequest request) throws AgentClientException;

    public boolean ping(String host);
}
