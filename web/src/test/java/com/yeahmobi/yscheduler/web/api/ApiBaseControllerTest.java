package com.yeahmobi.yscheduler.web.api;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.yeahmobi.yscheduler.web.common.HttpServletRequestMapWrapper;

public class ApiBaseControllerTest extends ApiControllerBaseTest {

    @Test
    public void testWithoutAppKey() {
        MockApiController mockApiController = new MockApiController();
        HttpServletRequestMapWrapper request = createRequest();
        request.put("token", "fjwoiejfo");

        assertResponse(ApiStatusCode.AUTH_FAILED, "appKey must not be null or empty", null,
                       mockApiController.success(request));
    }

    @Test
    public void testWithoutToken() {
        MockApiController mockApiController = new MockApiController();
        HttpServletRequestMapWrapper request = createRequest();
        request.put("appKey", "fjwoiejfo");

        assertResponse(ApiStatusCode.AUTH_FAILED, "token must not be null or empty", null,
                       mockApiController.success(request));
    }

    @Test
    public void testBizError() {
        MockApiController mockApiController = new MockApiController();
        HttpServletRequestMapWrapper request = createRequest();
        request.put("appKey", "fjwoiejfo");
        request.put("token", "fjwoiejfo");

        assertResponse(ApiStatusCode.BIZ_ERROR, "biz error", null, mockApiController.bizFail(request));
    }

    @Test
    public void testHandlerException() {
        MockApiController mockApiController = new MockApiController();
        HttpServletRequestMapWrapper request = createRequest();
        request.put("appKey", "fjwoiejfo");
        request.put("token", "fjwoiejfo");

        assertResponse(ApiStatusCode.UNKNOWN_EXCEPTION, "test exception", null, mockApiController.exception(request));
    }

    @Test
    public void testSuccess() {
        MockApiController mockApiController = new MockApiController();
        HttpServletRequestMapWrapper request = createRequest();
        request.put("appKey", "fjwoiejfo");
        request.put("token", "fjwoiejfo");

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("key", "value");
        assertResponse(ApiStatusCode.SUCCESS, null, map, mockApiController.success(request));
    }

    private static class MockApiController extends ApiBaseController {

        public String bizFail(HttpServletRequestMapWrapper request) {
            return handleRequest(request, new ApiHandler() {

                public void handle(ApiRequest apiRequest, ApiResponse apiResponse) throws Exception {
                    apiResponse.setMessage("biz error");
                    apiResponse.setStatus(ApiStatusCode.BIZ_ERROR);
                }
            });
        }

        public String exception(HttpServletRequestMapWrapper request) {
            return handleRequest(request, new ApiHandler() {

                public void handle(ApiRequest apiRequest, ApiResponse apiResponse) throws Exception {
                    throw new Exception("test exception");
                }
            });
        }

        public String success(HttpServletRequestMapWrapper request) {
            return handleRequest(request, new ApiHandler() {

                public void handle(ApiRequest apiRequest, ApiResponse apiResponse) throws Exception {
                    apiResponse.setStatus(ApiStatusCode.SUCCESS);
                    apiResponse.addReturnValue("key", "value");
                }
            });
        }
    }
}
