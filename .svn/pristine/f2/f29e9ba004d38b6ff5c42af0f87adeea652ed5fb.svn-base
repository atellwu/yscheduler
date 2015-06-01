package com.yeahmobi.yscheduler.loadbalance;

import com.yeahmobi.yscheduler.model.Agent;

public interface AgentLoadbalance {

    // AttemptExecutor逻辑: 如果 task 配置了 agentId，则直接查；如果配了group，才会调用该方法，获取一个agent
    /** 从指定agent group组获取可用一个agent(排除disable和非active的) */
    Agent getActiveAgent(long teamId);

}
