package com.yeahmobi.yscheduler.web.api;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Leo Liang
 */
public class ApiResponse {

    private ApiStatusCode       status      = ApiStatusCode.SUCCESS;
    private String              message;
    private Map<String, Object> returnValue = new HashMap<String, Object>();

    public int getStatus() {
        return this.status.code();
    }

    public void setStatus(ApiStatusCode status) {
        this.status = status;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getReturnValue() {
        return this.returnValue;
    }

    public void addReturnValue(String key, Object value) {
        this.returnValue.put(key, value);
    }

    public boolean isSuccess() {
        return ApiStatusCode.SUCCESS.equals(this.status);
    }
}
