package com.yeahmobi.yscheduler.web.vo;

import java.util.Date;

import com.yeahmobi.yscheduler.model.Workflow;
import com.yeahmobi.yscheduler.model.type.WorkflowStatus;

public class WorkflowVO {

    private Workflow workflow;

    private String   owner;

    public WorkflowVO(Workflow workflow, String owner) {
        this.workflow = workflow;
        this.owner = owner;
    }

    public Long getId() {
        return this.workflow.getId();
    }

    public String getName() {
        return this.workflow.getName();
    }

    public String getOwner() {
        return this.owner;
    }

    public String getCrontab() {
        return this.workflow.getCrontab();
    }

    public WorkflowStatus getStatus() {
        return this.workflow.getStatus();
    }

    public Integer getTimeout() {
        return this.workflow.getTimeout();
    }

    public String getDescription() {
        return this.workflow.getDescription();
    }

    public Date getCreateTime() {
        return this.workflow.getCreateTime();
    }

    public Date getUpdateTime() {
        return this.workflow.getUpdateTime();
    }

}
