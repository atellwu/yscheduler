package com.yeahmobi.yscheduler.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.MDC;

public class Log4jMDCFilter implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
                                                                                             ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        StringBuilder uriWithQueryString = new StringBuilder(req.getRequestURI());
        String queryString = req.getQueryString();
        if (queryString != null) {
            uriWithQueryString.append("?").append(queryString);
        }
        MDC.put("req.uriWithQueryString", uriWithQueryString.toString());
        try {
            chain.doFilter(request, response);
        } finally {
            MDC.remove("req.uriWithQueryString");
        }
    }

    public void destroy() {
    }
}
