package com.yeahmobi.yscheduler.web.vo;

import java.util.Date;

import com.yeahmobi.yscheduler.model.Attempt;
import com.yeahmobi.yscheduler.model.TaskInstance;
import com.yeahmobi.yscheduler.model.type.TaskInstanceStatus;

public class WorkflowInstanceTasksVO {

    private TaskInstance taskInstance;
    private String       taskName;
    private Date         endTime;
    private Attempt      attempt;

    public WorkflowInstanceTasksVO(TaskInstance taskInstance) {
        super();
        this.taskInstance = taskInstance;
    }

    /**
     * @return the attempt
     */
    public Attempt getAttempt() {
        return this.attempt;
    }

    /**
     * @param attempt the attempt to set
     */
    public void setAttempt(Attempt attempt) {
        this.attempt = attempt;
    }

    public Date getEndTime() {
        return this.endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getTaskName() {
        return this.taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Long getId() {
        return this.taskInstance.getId();
    }

    public Long getTaskId() {
        return this.taskInstance.getTaskId();
    }

    public Long getWorkflowInstanceId() {
        return this.taskInstance.getWorkflowInstanceId();
    }

    public TaskInstanceStatus getStatus() {
        return this.taskInstance.getStatus();
    }

    public Date getStartTime() {
        return this.taskInstance.getStartTime();
    }

    public Date getCreateTime() {
        return this.taskInstance.getCreateTime();
    }

    public Date getUpdateTime() {
        return this.taskInstance.getUpdateTime();
    }

}
