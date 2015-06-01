package com.yeahmobi.yscheduler.agentframework.agent.task;

/**
 * @author Leo.Liang
 */
public enum TaskTransactionStatus {
    INIT, //
    RUNNING, //
    FAIL, //
    SUCCESS, //
    CANCEL, //
    COMPLETE_WITH_UNKNOWN_STATUS, //
    ;

    public boolean isCompleted() {
        return FAIL.equals(this) || SUCCESS.equals(this) || CANCEL.equals(this)
               || COMPLETE_WITH_UNKNOWN_STATUS.equals(this);
    }
}
