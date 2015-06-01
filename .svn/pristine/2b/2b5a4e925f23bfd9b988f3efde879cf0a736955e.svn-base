package com.yeahmobi.yscheduler.agentframework.agent.task;

/**
 * @author Leo.Liang
 */
public abstract class BaseTaskExecutor<T extends AgentTask> implements TaskExecutor<T> {

    public Integer recover(TaskTransaction<T> taskTransaction) throws Exception {
        return execute(taskTransaction);
    }

}
