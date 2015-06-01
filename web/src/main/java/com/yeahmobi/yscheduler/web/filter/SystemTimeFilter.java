package com.yeahmobi.yscheduler.web.filter;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class SystemTimeFilter implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
                                                                                             ServletException {
        HttpServletResponse resp = (HttpServletResponse) response;
        resp.addHeader("systemTime", new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
        String timeZone = URLEncoder.encode(Calendar.getInstance().getTimeZone().getDisplayName(), "UTF-8");
        resp.addHeader("timeZone", timeZone);
        chain.doFilter(request, resp);
    }

    public void destroy() {
    }
}
