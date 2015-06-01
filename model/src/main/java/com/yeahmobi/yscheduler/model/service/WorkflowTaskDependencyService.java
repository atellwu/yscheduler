package com.yeahmobi.yscheduler.model.service;

import java.util.List;

import com.yeahmobi.yscheduler.model.WorkflowTaskDependency;

public interface WorkflowTaskDependencyService {

    List<WorkflowTaskDependency> listByWorkflowDetailId(long workflowDetailId);

    void deleteByWorkflowDetailId(long workflowDetailId);

    void addDependencyTasks(long workflowDetailId, List<Long> dependencyTasks);

}
