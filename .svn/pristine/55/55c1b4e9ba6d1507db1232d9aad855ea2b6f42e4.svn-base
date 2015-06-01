package com.yeahmobi.yscheduler.web.api;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.yeahmobi.yscheduler.web.common.HttpServletRequestMapWrapper;

public class ApiBaseController {

    protected static final String REQ_KEY_APPKEY = "appKey";
    protected static final String REQ_KEY_TOKEN  = "token";

    protected String handleRequest(HttpServletRequestMapWrapper request, ApiHandler handler) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatus(ApiStatusCode.SUCCESS);
        ApiRequest apiRequest = createApiRequest(request, apiResponse);

        if (apiResponse.isSuccess()) {
            if (auth(apiRequest, apiResponse)) {
                try {
                    handler.handle(apiRequest, apiResponse);
                } catch (Exception e) {
                    apiResponse.setStatus(ApiStatusCode.UNKNOWN_EXCEPTION);
                    apiResponse.setMessage(e.getMessage());
                }
            }
        }

        return JSON.toJSONString(apiResponse);
    }

    protected boolean auth(ApiRequest apiRequest, ApiResponse apiReqponse) {
        return true;
    }

    protected ApiRequest createApiRequest(HttpServletRequestMapWrapper request, ApiResponse apiResponse) {
        String appKey = request.get(REQ_KEY_APPKEY);
        String token = request.get(REQ_KEY_TOKEN);

        if (StringUtils.isBlank(appKey)) {
            apiResponse.setStatus(ApiStatusCode.AUTH_FAILED);
            apiResponse.setMessage("appKey must not be null or empty");
            return null;
        }

        if (StringUtils.isBlank(token)) {
            apiResponse.setStatus(ApiStatusCode.AUTH_FAILED);
            apiResponse.setMessage("token must not be null or empty");
            return null;
        }

        return new ApiRequest(appKey, token);

    }

    protected interface ApiHandler {

        public void handle(ApiRequest apiRequest, ApiResponse apiResponse) throws Exception;
    }
}
