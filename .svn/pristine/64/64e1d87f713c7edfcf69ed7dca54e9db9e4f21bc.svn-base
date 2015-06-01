package com.yeahmobi.yscheduler.model.service;

import java.util.List;

/**
 * @author Ryan Sun
 */
public interface TaskAuthorityService {

    public List<Long> listReadonlyUser(long taskId);

    public List<Long> listReadonlyTaskIds(long userId);

    public List<Long> listWritableUser(long taskId);

    public List<Long> listWritableTaskIds(long userId);

    public List<Long> listFollowUser(long taskId);

    void add(List<Long> readableUsers, List<Long> writableUsers, List<Long> followingUsers, long taskId);

    void update(List<Long> readableUsers, List<Long> writableUsers, List<Long> followingUsers, long taskId);

    void deleteByUser(long userId);
}
