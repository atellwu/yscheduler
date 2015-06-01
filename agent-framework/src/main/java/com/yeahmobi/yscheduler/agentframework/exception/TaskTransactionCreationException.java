package com.yeahmobi.yscheduler.agentframework.exception;

/**
 * @author Leo.Liang
 */
public class TaskTransactionCreationException extends Exception {

    public TaskTransactionCreationException() {
        super();
    }

    public TaskTransactionCreationException(String message) {
        super(message);
    }

    public TaskTransactionCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public TaskTransactionCreationException(Throwable cause) {
        super(cause);
    }
}
