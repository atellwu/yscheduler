package com.yeahmobi.yscheduler.agentframework.exception;

/**
 * @author Leo.Liang
 */
public class TaskNotFoundException extends Exception {

    public TaskNotFoundException() {
        super();
    }

    public TaskNotFoundException(String message) {
        super(message);
    }

    public TaskNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public TaskNotFoundException(Throwable cause) {
        super(cause);
    }
}
