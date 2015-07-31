package com.yeahmobi.yscheduler.web.vo;

import java.util.Date;

import com.yeahmobi.yscheduler.model.WorkflowTaskInstance;
import com.yeahmobi.yscheduler.model.type.ScheduleType;
import com.yeahmobi.yscheduler.model.type.TaskInstanceStatus;
import com.yeahmobi.yscheduler.model.type.WorkflowInstanceStatus;

public class WorkflowTaskInstanceVO {

    private WorkflowTaskInstance instance;

    public WorkflowTaskInstanceVO(WorkflowTaskInstance instance) {
        super();
        this.instance = instance;
    }

    public Long getId() {
        return this.instance.getId();
    }

    public void setId(Long id) {
        this.instance.setId(id);
    }

    public ScheduleType getType() {
        return this.instance.getType();
    }

    public void setType(ScheduleType type) {
        this.instance.setType(type);
    }

    public Long getWorkflowTaskId() {
        return this.instance.getWorkflowTaskId();
    }

    public void setWorkflowTaskId(Long workflowTaskId) {
        this.instance.setWorkflowTaskId(workflowTaskId);
    }

    public Object getStatus() {
        switch (this.instance.getType()) {
            case TASK:
                return TaskInstanceStatus.valueOf(this.instance.getStatus());
            case WORKFLOW:
                return WorkflowInstanceStatus.valueOf(this.instance.getStatus());
        }
        return this.instance.getStatus();
    }

    public void setStatus(Byte status) {
        this.instance.setStatus(status);
    }

    public Date getScheduleTime() {
        return this.instance.getScheduleTime();
    }

    public void setScheduleTime(Date scheduleTime) {
        this.instance.setScheduleTime(scheduleTime);
    }

    public Date getStartTime() {
        return this.instance.getStartTime();
    }

    public void setStartTime(Date startTime) {
        this.instance.setStartTime(startTime);
    }

    public Date getEndTime() {
        return this.instance.getEndTime();
    }

    public void setEndTime(Date endTime) {
        this.instance.setEndTime(endTime);
    }

    public Date getCreateTime() {
        return this.instance.getCreateTime();
    }

    public void setCreateTime(Date createTime) {
        this.instance.setCreateTime(createTime);
    }

    public Date getUpdateTime() {
        return this.instance.getUpdateTime();
    }

    public void setUpdateTime(Date updateTime) {
        this.instance.setUpdateTime(updateTime);
    }

    public String getName() {
        return this.instance.getName();
    }

    public void setName(String name) {
        this.instance.setName(name);
    }

    public Long getOwner() {
        return this.instance.getOwner();
    }

    public void setOwner(Long owner) {
        this.instance.setOwner(owner);
    }

}
