package com.yeahmobi.yscheduler.agentframework.agent.task;

/**
 * @author Leo.Liang
 */
public class TaskStatus {

    private TaskTransactionStatus status;
    private long                  duration;
    private Integer               returnValue;

    public TaskStatus() {
    }

    public TaskStatus(TaskTransactionStatus status, long duration, Integer returnValue) {
        super();
        this.status = status;
        this.duration = duration;
        this.returnValue = returnValue;
    }

    /**
     * @return the status
     */
    public TaskTransactionStatus getStatus() {
        return this.status;
    }

    /**
     * @return the returnValue
     */
    public Integer getReturnValue() {
        return this.returnValue;
    }

    /**
     * @param returnValue the returnValue to set
     */
    public void setReturnValue(Integer returnValue) {
        this.returnValue = returnValue;
    }

    /**
     * @return the duration
     */
    public long getDuration() {
        return this.duration;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(TaskTransactionStatus status) {
        this.status = status;
    }

    /**
     * @param duration the duration to set
     */
    public void setDuration(long duration) {
        this.duration = duration;
    }

}
