package com.yeahmobi.yscheduler.model.service;

import java.util.Date;
import java.util.List;

import com.yeahmobi.yscheduler.common.Paginator;
import com.yeahmobi.yscheduler.model.Workflow;
import com.yeahmobi.yscheduler.model.common.Query;
import com.yeahmobi.yscheduler.model.type.WorkflowStatus;

public interface WorkflowService {

    void createOrUpdate(Workflow workflow);

    Workflow get(long id);

    List<Workflow> list(Query query, int pageNum, Paginator paginator, long userId, boolean common);

    List<Workflow> listAll(WorkflowStatus status);

    List<Workflow> listAllPrivate();

    List<Workflow> listAllCommon();

    boolean canModify(long workflowId, long userId);

    boolean nameExist(String name);

    void updateScheduleTime(long workflowId, Date time);
}
