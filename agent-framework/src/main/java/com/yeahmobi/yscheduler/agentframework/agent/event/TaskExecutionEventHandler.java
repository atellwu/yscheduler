package com.yeahmobi.yscheduler.agentframework.agent.event;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.yeahmobi.yscheduler.agentframework.agent.task.TaskExecutionContainer;
import com.yeahmobi.yscheduler.agentframework.exception.TaskNotFoundException;

/**
 * @author Leo.Liang
 */
public abstract class TaskExecutionEventHandler implements EventHandler, ApplicationContextAware {

    protected static final String PARAM_TX_ID  = "txId";
    protected static final String PARAM_OFFSET = "offset";
    protected static final String PARAM_LENGTH = "length";
    private ApplicationContext    applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    protected TaskExecutionContainer getTaskExecutionContainer() {
        return this.applicationContext.getBean(TaskExecutionContainer.class);
    }

    private boolean validateTransactionId(String txId, HandlerResult handlerResult) {
        if (StringUtils.isBlank(txId)) {
            handlerResult.setSuccess(false);
            handlerResult.setErrorMsg("Parameter txId can not be empty or null.");
            return false;
        } else if (!StringUtils.isNumeric(txId)) {
            handlerResult.setSuccess(false);
            handlerResult.setErrorMsg("Parameter txId  must be a number.");
            return false;
        }

        return true;
    }

    protected void cancel(Map<String, String> params, HandlerResult handlerResult) {
        String txId = params.get(PARAM_TX_ID);
        if (!validateTransactionId(txId, handlerResult)) {
            return;
        }

        try {
            this.getTaskExecutionContainer().cancel(Long.valueOf(txId));
            handlerResult.setSuccess(true);
        } catch (TaskNotFoundException e) {
            handlerResult.setSuccess(false);
            handlerResult.setThrowable(e);
            handlerResult.setErrorMsg(e.getMessage());
        }
    }

    protected void checkStatus(Map<String, String> params, HandlerResult handlerResult) {
        String txId = params.get(PARAM_TX_ID);
        if (!validateTransactionId(txId, handlerResult)) {
            return;
        }

        try {
            handlerResult.setResult(this.getTaskExecutionContainer().checkStatus(Long.valueOf(txId)));
            handlerResult.setSuccess(true);
        } catch (TaskNotFoundException e) {
            handlerResult.setSuccess(false);
            handlerResult.setThrowable(e);
            handlerResult.setErrorMsg(e.getMessage());
        }
    }

}
