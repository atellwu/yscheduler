package com.yeahmobi.yscheduler.agentframework.zookeeper;

import com.yeahmobi.yscheduler.model.type.AttemptStatus;

public interface StatusChangedListener {

    void onChange(long agentId, long attemptId, AttemptStatus status);
}
