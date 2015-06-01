package com.yeahmobi.yscheduler.agentframework.agent.event;

import java.util.Map;

import com.yeahmobi.yscheduler.agentframework.agent.task.AgentTask;

/**
 * @author Leo.Liang
 */
public abstract class TaskSubmitionEventHandler extends TaskExecutionEventHandler {

    public void onEvent(Map<String, String> params, HandlerResult handlerResult) {
        try {
            AgentTask task = getTask(params);
            long txId = this.getTaskExecutionContainer().submit(task);

            handlerResult.setSuccess(true);
            handlerResult.setResult(txId);
            afterSubmit(params, handlerResult);

        } catch (IllegalArgumentException e) {
            handlerResult.setSuccess(false);
            handlerResult.setErrorMsg("Parameter invalid: " + e.getMessage());
        } catch (Throwable e) {
            handlerResult.setSuccess(false);
            handlerResult.setErrorMsg("Exception occurs while handle event");
            handlerResult.setThrowable(e);
        } finally {
            beforeReturn(params, handlerResult);
        }
    }

    protected void beforeReturn(Map<String, String> params, HandlerResult result) {

    }

    protected void afterSubmit(Map<String, String> params, HandlerResult result) {

    }

    /**
     * @throws IllegalArgumentException 参数不合法
     */
    public abstract AgentTask getTask(Map<String, String> params) throws IllegalArgumentException;

}
