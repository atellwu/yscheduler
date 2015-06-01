package com.yeahmobi.yscheduler.web.vo;

import java.util.Date;

import com.yeahmobi.yscheduler.model.Task;
import com.yeahmobi.yscheduler.model.User;
import com.yeahmobi.yscheduler.model.type.TaskStatus;
import com.yeahmobi.yscheduler.model.type.TaskType;

public class TaskVO {

    private Long       id;

    private String     name;

    private Long       owner;

    private TaskType   type;

    private String     crontab;

    private Long       agentId;

    private TaskStatus status;

    private Integer    timeout;

    private Integer    retryTimes;

    private String     description;

    private Date       createTime;

    private Date       updateTime;

    private String     command;

    private User       user;

    public TaskVO(Task task) {
        this.id = task.getId();
        this.name = task.getName();
        this.owner = task.getOwner();
        this.type = task.getType();
        this.crontab = task.getCrontab();
        this.agentId = task.getAgentId();
        this.status = task.getStatus();
        this.timeout = task.getTimeout();
        this.retryTimes = task.getRetryTimes();
        this.description = task.getDescription();
        this.createTime = task.getCreateTime();
        this.updateTime = task.getUpdateTime();
        this.command = task.getCommand();
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getOwner() {
        return this.owner;
    }

    public void setOwner(Long owner) {
        this.owner = owner;
    }

    public TaskType getType() {
        return this.type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    public String getCrontab() {
        return this.crontab;
    }

    public void setCrontab(String crontab) {
        this.crontab = crontab;
    }

    public Long getAgentId() {
        return this.agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public TaskStatus getStatus() {
        return this.status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public Integer getTimeout() {
        return this.timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Integer getRetryTimes() {
        return this.retryTimes;
    }

    public void setRetryTimes(Integer retryTimes) {
        this.retryTimes = retryTimes;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getCommand() {
        return this.command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
