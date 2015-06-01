package com.yeahmobi.yscheduler.agentframework.exception;

/**
 * @author Leo.Liang
 */
public class TaskTransactionManagerInitializeFailException extends Exception {

    public TaskTransactionManagerInitializeFailException() {
        super();
    }

    public TaskTransactionManagerInitializeFailException(String message) {
        super(message);
    }

    public TaskTransactionManagerInitializeFailException(String message, Throwable cause) {
        super(message, cause);
    }

    public TaskTransactionManagerInitializeFailException(Throwable cause) {
        super(cause);
    }
}
