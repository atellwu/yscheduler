package com.yeahmobi.yscheduler.web.common;

import java.util.Map;

public class HttpServletRequestMapWrapper {

    private Map<String, String> map;

    public HttpServletRequestMapWrapper(Map<String, String> map) {
        super();
        this.map = map;
    }

    public String get(String key) {
        return this.map.get(key);
    }

    public void put(String key, String value) {
        this.map.put(key, value);
    }
}
