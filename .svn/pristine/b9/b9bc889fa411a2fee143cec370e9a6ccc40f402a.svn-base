package com.yeahmobi.yscheduler.agentframework.zookeeper;

import java.util.List;

public interface AgentManager {

    /**
     * 根据agent host获取agent id
     */
    long getAgentId(String host);

    /**
     * 心跳
     */
    void active(long agentId);

    /**
     * 获取agent active list
     */
    List<Long> activeList();

    /**
     * 获取下一个可用agent（loadbalance）
     */
    long nextActive();
}
