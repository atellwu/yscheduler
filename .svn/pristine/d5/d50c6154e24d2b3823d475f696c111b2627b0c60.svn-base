package com.yeahmobi.yscheduler.agentframework.zookeeper;

import java.util.List;

import com.yeahmobi.yscheduler.model.Agent;

public interface AgentManager {

    /**
     * 根据agent host获取agent id
     */
    long getAgentId(String host) throws Exception;

    /**
     * 心跳
     */
    void active(long agentId) throws Exception;

    /**
     * 获取agent active list
     */
    List<Long> activeList() throws Exception;

    /**
     * 获取下一个可用agent（loadbalance）
     */
    long nextActive() throws Exception;

    void addAgent(Agent agent) throws Exception;

    void delAgent(Agent agent) throws Exception;

    void modAgent(Agent agent) throws Exception;
}
