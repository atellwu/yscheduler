package com.yeahmobi.yscheduler.agentframework.exception;

/**
 * @author Leo.Liang
 */
public class AgentClientException extends Exception {

    public AgentClientException() {
        super();
    }

    public AgentClientException(String message) {
        super(message);
    }

    public AgentClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public AgentClientException(Throwable cause) {
        super(cause);
    }
}
