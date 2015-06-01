package com.yeahmobi.yscheduler.agentframework.zookeeper;

import com.yeahmobi.yscheduler.model.Attempt;
import com.yeahmobi.yscheduler.model.type.AttemptStatus;

public interface AssignmentManager {

    void assignTo(long agentId, Attempt attempt);

    void addAssignmentListener(AssignmentListener listener);

    void changeStatus(long agentId, long attemptId, AttemptStatus status);

    void addStatusChangedListener(StatusChangedListener listener);

    void cancel(long agentId, long attemptId);

    void addCancelListener(CancelListener listener);

}
