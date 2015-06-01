package com.yeahmobi.yscheduler.model.service;

import java.util.List;

import com.yeahmobi.yscheduler.model.Task;
import com.yeahmobi.yscheduler.model.WorkflowDetail;

public interface WorkflowDetailService {

    void save(long workflowId, List<WorkflowDetail> workflowDetails, List<List<Long>> dependencyList);

    void save(long workflowId, List<WorkflowDetail> workflowDetails, List<List<Long>> dependencyList, long userId);

    void updateDelayTime(long workflowId, long userId, int delay);

    List<WorkflowDetail> list(long workflowId);

    List<WorkflowDetail> list(long workflowId, long userId);

    List<WorkflowDetail> listAllExceptRootTask(long workflowId);

    List<Long> listDependencyTaskIds(long workflowId, long taskId);

    WorkflowDetail get(long workflowId, long taskId);

    int getTeamDelay(long workflowId, long userId);

    void addTeamRootTask(Task task);

}
