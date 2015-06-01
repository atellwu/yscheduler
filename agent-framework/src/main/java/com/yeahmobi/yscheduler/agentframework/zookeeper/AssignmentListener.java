package com.yeahmobi.yscheduler.agentframework.zookeeper;

import com.yeahmobi.yscheduler.model.Attempt;

public interface AssignmentListener {

    void onAssign(long agentId, Attempt attempt);
}
