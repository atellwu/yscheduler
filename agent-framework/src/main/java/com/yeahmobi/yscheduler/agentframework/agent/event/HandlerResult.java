/**
 *
 */
package com.yeahmobi.yscheduler.agentframework.agent.event;

/**
 * @author Leo.Liang
 */
public class HandlerResult {

    private boolean   success = true;
    private String    errorMsg;
    private Throwable throwable;
    private Object    result;

    public Throwable getThrowable() {
        return this.throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    /**
     * @return the success
     */
    public boolean isSuccess() {
        return this.success;
    }

    /**
     * @param success the success to set
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * @return the errorMsg
     */
    public String getErrorMsg() {
        return this.errorMsg;
    }

    /**
     * @param errorMsg the errorMsg to set
     */
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    /**
     * @return the result
     */
    public Object getResult() {
        return this.result;
    }

    /**
     * @param result the result to set
     */
    public void setResult(Object result) {
        this.result = result;
    }

}
