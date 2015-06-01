package com.yeahmobi.yscheduler.agentframework.agent.task;

import com.yeahmobi.yscheduler.agentframework.exception.TaskNotFoundException;
import com.yeahmobi.yscheduler.agentframework.exception.TaskSubmitException;

/**
 * @author Leo.Liang
 */
public interface TaskExecutionContainer {

    public long submit(AgentTask task) throws TaskSubmitException;

    public TaskStatus checkStatus(long transactionId) throws TaskNotFoundException;

    public void cancel(long transactionId) throws TaskNotFoundException;
}
