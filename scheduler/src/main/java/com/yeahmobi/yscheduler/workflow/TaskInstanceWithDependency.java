package com.yeahmobi.yscheduler.workflow;

import java.util.List;

import com.yeahmobi.yscheduler.model.Task;
import com.yeahmobi.yscheduler.model.TaskInstance;

/**
 * @author Ryan Sun
 */
public class TaskInstanceWithDependency {

    private Task         task;

    private Long         teamId;

    private TaskInstance taskInstance;

    private List<Long>   dependencies;

    public TaskInstanceWithDependency(Task task, Long teamId, TaskInstance taskInstance, List<Long> dependencies) {
        super();
        this.task = task;
        this.teamId = teamId;
        this.taskInstance = taskInstance;
        this.dependencies = dependencies;
    }

    public Task getTask() {
        return this.task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Long getTeamId() {
        return this.teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public TaskInstance getTaskInstance() {
        return this.taskInstance;
    }

    public void setTaskInstance(TaskInstance taskInstance) {
        this.taskInstance = taskInstance;
    }

    public List<Long> getDependencies() {
        return this.dependencies;
    }

    public void setDependencies(List<Long> dependencies) {
        this.dependencies = dependencies;
    }

}
