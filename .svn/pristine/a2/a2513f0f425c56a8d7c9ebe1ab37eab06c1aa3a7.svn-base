package com.yeahmobi.yscheduler.web.common;

import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class HttpServletRequestHashMapArgumentResolver implements HandlerMethodArgumentResolver {

    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(HttpServletRequestHashMap.class) != null;
    }

    @SuppressWarnings("unchecked")
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

        HashMap<String, String> map = new HashMap<String, String>();

        Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            map.put(name, request.getParameter(name));
        }

        names = request.getHeaderNames();

        while (names.hasMoreElements()) {
            String name = names.nextElement();
            map.put("header." + name, request.getHeader(name));
        }

        return new HttpServletRequestMapWrapper(map);
    }

}
