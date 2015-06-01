package com.yeahmobi.yscheduler.web.vo;

import java.util.List;

import com.yeahmobi.yscheduler.model.WorkflowDetail;

/**
 * @author Ryan Sun
 */
public class WorkflowDetailVO {

    private WorkflowDetail workflowDetail;

    private String         taskName;

    private Long           teamId;

    private List<Long>     dependencies;

    private boolean        needValidate = true;

    public WorkflowDetail getWorkflowDetail() {
        return this.workflowDetail;
    }

    public void setWorkflowDetail(WorkflowDetail workflowDetail) {
        this.workflowDetail = workflowDetail;
    }

    public List<Long> getDependencies() {
        return this.dependencies;
    }

    public void setDependencies(List<Long> dependencies) {
        this.dependencies = dependencies;
    }

    public String getTaskName() {
        return this.taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Long getTeamId() {
        return this.teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    @Override
    public String toString() {
        return String.format("WorkflowDetailVO [workflowDetail=%s, taskName=%s, teamId=%s, dependencies=%s]",
                             this.workflowDetail, this.taskName, this.teamId, this.dependencies);
    }

    public boolean getNeedValidate() {
        return this.needValidate;
    }

    public void setNeedValidate(boolean needValidate) {
        this.needValidate = needValidate;
    }

}
