package com.yeahmobi.yscheduler.model.service;

import java.util.Date;
import java.util.List;

import com.yeahmobi.yscheduler.common.Paginator;
import com.yeahmobi.yscheduler.model.Agent;
import com.yeahmobi.yscheduler.model.Task;
import com.yeahmobi.yscheduler.model.common.NameValuePair;
import com.yeahmobi.yscheduler.model.common.Query;
import com.yeahmobi.yscheduler.model.type.TaskStatus;

public interface TaskService {

    void add(Task task);

    void update(Task task);

    Task get(long id);

    List<Task> list(Query query, int pageNum, Paginator paginator, long userId);

    List<Task> listHeartbeatTask();

    List<Task> list(TaskStatus status);

    List<NameValuePair> list(long userId);

    List<NameValuePair> list();

    boolean canModify(long taskId, long userId);

    boolean nameExist(String name);

    void updateLastScheduleTime(Long id, Date scheduleTime);

    boolean hasTaskAttachedToAgent(long agentId);

    boolean hasTaskAttachedToTeamAgents(long teamId);

    void addHeartbeatTask(Agent agent);

    void removeHeartbeatTask(long agentId, String name);

    void addUpgradeTask(Agent agent);

    /** 物理删除task */
    void removeTask(long taskId);

    Task get(String taskName);

    Task updateTeamRootTaskName(String name, String newName);

    Task addRootTaskIfAbsent(String teamName);

    Task getRootTask(String teamName);

    List<Task> listRootTasks();

    boolean isRootTask(long id);

    boolean isRootTask(String taskName);

    void updateAttachment(long taskId, String filename, Long version);

    void updateAgentId(long taskId, Long agentId);

}
