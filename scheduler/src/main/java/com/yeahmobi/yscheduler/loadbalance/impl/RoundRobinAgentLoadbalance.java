package com.yeahmobi.yscheduler.loadbalance.impl;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeahmobi.yscheduler.loadbalance.AgentLoadbalance;
import com.yeahmobi.yscheduler.model.Agent;
import com.yeahmobi.yscheduler.model.service.AgentService;
import com.yeahmobi.yscheduler.monitor.ActiveAgentManager;

@Service
public class RoundRobinAgentLoadbalance implements AgentLoadbalance {

    private AtomicInteger      index = new AtomicInteger(0);

    @Autowired
    private ActiveAgentManager activeAgentManager;

    @Autowired
    private AgentService       agentService;

    public Agent getActiveAgent(long teamId) {
        // 获取enable的agent list
        // List<Agent> list = this.agentService.list(teamId, true);
        List<Agent> list = this.activeAgentManager.getActiveList(teamId);
        if ((list == null) || (list.size() == 0)) {
            throw new IllegalStateException(String.format("No active agent by team(%s)", teamId));
        }
        // 获取
        return list.get(this.index.getAndIncrement() % list.size());
    }

}
