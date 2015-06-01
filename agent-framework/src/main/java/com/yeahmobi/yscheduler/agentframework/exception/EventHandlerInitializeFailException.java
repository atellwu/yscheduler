package com.yeahmobi.yscheduler.agentframework.exception;

/**
 * @author Leo.Liang
 */
public class EventHandlerInitializeFailException extends RuntimeException {

    public EventHandlerInitializeFailException() {
        super();
    }

    public EventHandlerInitializeFailException(String message) {
        super(message);
    }

    public EventHandlerInitializeFailException(String message, Throwable cause) {
        super(message, cause);
    }

    public EventHandlerInitializeFailException(Throwable cause) {
        super(cause);
    }
}
