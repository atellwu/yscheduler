package com.yeahmobi.yscheduler.agentframework.agent.task;

import java.util.List;

import com.yeahmobi.yscheduler.agentframework.exception.TaskNotFoundException;
import com.yeahmobi.yscheduler.agentframework.exception.TaskTransactionCreationException;

/**
 * @author Leo.Liang
 */
public interface TaskTransactionManager {

    public TaskTransaction getTransaction(long transactionId) throws TaskNotFoundException;

    public TaskTransaction getTransaction(long transactionId, AgentTask task) throws TaskTransactionCreationException,
                                                                             TaskNotFoundException;

    public TaskTransaction createTransaction(AgentTask task) throws TaskTransactionCreationException;

    public List<TaskTransaction> getAllTransaction() throws TaskNotFoundException;
}
