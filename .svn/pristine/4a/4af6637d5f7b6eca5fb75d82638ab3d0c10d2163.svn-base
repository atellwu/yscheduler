/**
 *
 */
package com.yeahmobi.yscheduler.agentframework;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author Leo.Liang
 */
public class AgentResponse<T> {

    public static final String FIELD_RESPONSE_DATA = "responseZData";
    private AgentResponseCode  responseCode        = AgentResponseCode.SUCCESS;
    private String             errorMsg;
    private String             responseType;
    private T                  responseData;
    private Throwable          throwable;

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
     * @return the responseCode
     */
    public AgentResponseCode getResponseCode() {
        return this.responseCode;
    }

    /**
     * @param responseCode the responseCode to set
     */
    public void setResponseCode(AgentResponseCode responseCode) {
        this.responseCode = responseCode;
    }

    /**
     * @return the responseData
     */
    @JSONField(name = FIELD_RESPONSE_DATA)
    public T getResponseData() {
        return this.responseData;
    }

    public void setResponseData(T responseData) {
        if (responseData != null) {
            this.responseType = responseData.getClass().getName();
            this.responseData = responseData;
        }
    }

    /**
     * @return the responseType
     */
    public String getResponseType() {
        return this.responseType;
    }

    /**
     * @param responseType the responseType to set
     */
    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }

    public Throwable getThrowable() {
        return this.throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

}
