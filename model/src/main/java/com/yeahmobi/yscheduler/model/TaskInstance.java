package com.yeahmobi.yscheduler.model;

import com.yeahmobi.yscheduler.model.type.TaskInstanceStatus;
import java.util.Date;

public class TaskInstance {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column task_instance.id
     *
     * @mbggenerated
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column task_instance.task_id
     *
     * @mbggenerated
     */
    private Long taskId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column task_instance.workflow_instance_id
     *
     * @mbggenerated
     */
    private Long workflowInstanceId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column task_instance.status
     *
     * @mbggenerated
     */
    private TaskInstanceStatus status;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column task_instance.schedule_time
     *
     * @mbggenerated
     */
    private Date scheduleTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column task_instance.start_time
     *
     * @mbggenerated
     */
    private Date startTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column task_instance.end_time
     *
     * @mbggenerated
     */
    private Date endTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column task_instance.create_time
     *
     * @mbggenerated
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column task_instance.update_time
     *
     * @mbggenerated
     */
    private Date updateTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column task_instance.id
     *
     * @return the value of task_instance.id
     *
     * @mbggenerated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column task_instance.id
     *
     * @param id the value for task_instance.id
     *
     * @mbggenerated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column task_instance.task_id
     *
     * @return the value of task_instance.task_id
     *
     * @mbggenerated
     */
    public Long getTaskId() {
        return taskId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column task_instance.task_id
     *
     * @param taskId the value for task_instance.task_id
     *
     * @mbggenerated
     */
    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column task_instance.workflow_instance_id
     *
     * @return the value of task_instance.workflow_instance_id
     *
     * @mbggenerated
     */
    public Long getWorkflowInstanceId() {
        return workflowInstanceId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column task_instance.workflow_instance_id
     *
     * @param workflowInstanceId the value for task_instance.workflow_instance_id
     *
     * @mbggenerated
     */
    public void setWorkflowInstanceId(Long workflowInstanceId) {
        this.workflowInstanceId = workflowInstanceId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column task_instance.status
     *
     * @return the value of task_instance.status
     *
     * @mbggenerated
     */
    public TaskInstanceStatus getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column task_instance.status
     *
     * @param status the value for task_instance.status
     *
     * @mbggenerated
     */
    public void setStatus(TaskInstanceStatus status) {
        this.status = status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column task_instance.schedule_time
     *
     * @return the value of task_instance.schedule_time
     *
     * @mbggenerated
     */
    public Date getScheduleTime() {
        return scheduleTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column task_instance.schedule_time
     *
     * @param scheduleTime the value for task_instance.schedule_time
     *
     * @mbggenerated
     */
    public void setScheduleTime(Date scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column task_instance.start_time
     *
     * @return the value of task_instance.start_time
     *
     * @mbggenerated
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column task_instance.start_time
     *
     * @param startTime the value for task_instance.start_time
     *
     * @mbggenerated
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column task_instance.end_time
     *
     * @return the value of task_instance.end_time
     *
     * @mbggenerated
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column task_instance.end_time
     *
     * @param endTime the value for task_instance.end_time
     *
     * @mbggenerated
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column task_instance.create_time
     *
     * @return the value of task_instance.create_time
     *
     * @mbggenerated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column task_instance.create_time
     *
     * @param createTime the value for task_instance.create_time
     *
     * @mbggenerated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column task_instance.update_time
     *
     * @return the value of task_instance.update_time
     *
     * @mbggenerated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column task_instance.update_time
     *
     * @param updateTime the value for task_instance.update_time
     *
     * @mbggenerated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table task_instance
     *
     * @mbggenerated
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", taskId=").append(taskId);
        sb.append(", workflowInstanceId=").append(workflowInstanceId);
        sb.append(", status=").append(status);
        sb.append(", scheduleTime=").append(scheduleTime);
        sb.append(", startTime=").append(startTime);
        sb.append(", endTime=").append(endTime);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append("]");
        return sb.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table task_instance
     *
     * @mbggenerated
     */
    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        TaskInstance other = (TaskInstance) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getTaskId() == null ? other.getTaskId() == null : this.getTaskId().equals(other.getTaskId()))
            && (this.getWorkflowInstanceId() == null ? other.getWorkflowInstanceId() == null : this.getWorkflowInstanceId().equals(other.getWorkflowInstanceId()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getScheduleTime() == null ? other.getScheduleTime() == null : this.getScheduleTime().equals(other.getScheduleTime()))
            && (this.getStartTime() == null ? other.getStartTime() == null : this.getStartTime().equals(other.getStartTime()))
            && (this.getEndTime() == null ? other.getEndTime() == null : this.getEndTime().equals(other.getEndTime()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table task_instance
     *
     * @mbggenerated
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getTaskId() == null) ? 0 : getTaskId().hashCode());
        result = prime * result + ((getWorkflowInstanceId() == null) ? 0 : getWorkflowInstanceId().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getScheduleTime() == null) ? 0 : getScheduleTime().hashCode());
        result = prime * result + ((getStartTime() == null) ? 0 : getStartTime().hashCode());
        result = prime * result + ((getEndTime() == null) ? 0 : getEndTime().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }
}