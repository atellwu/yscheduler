package com.yeahmobi.yscheduler.model.service;

import java.util.List;

import com.yeahmobi.yscheduler.model.WorkflowAuthority;

/**
 * @author Leo.Liang
 */
public interface WorkflowAuthorityService {

    List<WorkflowAuthority> listByWorkflow(long workflowId);

    List<WorkflowAuthority> listByUser(long userId);

    public List<Long> listReadonlyUser(long workflowId);

    public List<Long> listReadonlyWorkflowIds(long userId);

    public List<Long> listWritableUser(long workflowId);

    public List<Long> listWritableWorkflowIds(long userId);

    public List<Long> listFollowUser(long workflowId);

    public boolean writable(long workflowId, long userId);

    void add(List<Long> readableUsers, List<Long> writableUsers, List<Long> followingUsers, long workflowId);

    void update(List<Long> readableUsers, List<Long> writableUsers, List<Long> followingUsers, long workflowId);

    void deleteByUser(long userId);
}
