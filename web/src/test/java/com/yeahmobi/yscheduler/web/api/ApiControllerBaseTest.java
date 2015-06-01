package com.yeahmobi.yscheduler.web.api;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.yeahmobi.yscheduler.web.common.HttpServletRequestMapWrapper;

/**
 * @author Leo Liang
 */
public class ApiControllerBaseTest {

    private String DEFAULT_APP_KEY = "TestApp";
    private String DEFAULT_TOKEN   = "TestToken";

    protected void assertResponse(ApiStatusCode expectedStatus, String expectedMessage,
                                  Map<String, Object> expectedReturnValue, String actualResponseStr) {
        ApiResponse expectedApiResponse = new ApiResponse();
        expectedApiResponse.setMessage(expectedMessage);
        expectedApiResponse.setStatus(expectedStatus);
        if (expectedReturnValue != null) {
            for (Map.Entry<String, Object> entry : expectedReturnValue.entrySet()) {
                expectedApiResponse.addReturnValue(entry.getKey(), entry.getValue());
            }
        }
        Assert.assertEquals(JSON.toJSONString(expectedApiResponse), actualResponseStr);
    }

    protected HttpServletRequestMapWrapper createRequest() {
        Map<String, String> map = new HashMap<String, String>();
        HttpServletRequestMapWrapper request = new HttpServletRequestMapWrapper(map);
        return request;
    }

    protected void addCommonReqParam(HttpServletRequestMapWrapper request) {
        request.put("appKey", this.DEFAULT_APP_KEY);
        request.put("token", this.DEFAULT_TOKEN);
    }
}
