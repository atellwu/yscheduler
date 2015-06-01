package com.yeahmobi.yscheduler.model.service;

import java.util.List;

import com.yeahmobi.yscheduler.common.Paginator;
import com.yeahmobi.yscheduler.model.TeamWorkflowInstanceStatus;
import com.yeahmobi.yscheduler.model.common.Query;
import com.yeahmobi.yscheduler.model.type.WorkflowInstanceStatus;

public interface TeamWorkflowStatusInstanceService {

    List<TeamWorkflowInstanceStatus> list(Query query, long userId, long workflowId, int pageNum, Paginator paginator);

    void updateStatus(long teamId, long workflowInstanceId, WorkflowInstanceStatus status);

    void save(TeamWorkflowInstanceStatus status);

}
