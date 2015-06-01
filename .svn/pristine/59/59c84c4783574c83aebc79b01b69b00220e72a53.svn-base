package com.yeahmobi.yscheduler.web.filter;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.yeahmobi.yscheduler.model.User;
import com.yeahmobi.yscheduler.model.common.UserContext;
import com.yeahmobi.yscheduler.model.common.UserContextHolder;
import com.yeahmobi.yscheduler.model.service.UserService;
import com.yeahmobi.yscheduler.web.common.UserContextUtils;

/**
 * @author Leo.Liang
 */
public class UserContextFilter implements Filter {

    private static final String LOGIN_URL          = "/login";
    private static final String STATIC_URL         = "/static/";
    private static final String ASSETS_URL         = "/assets/";
    private static final String API_URL            = "/api/";
    private static final String PROFILE_URL        = "/profile";
    private static final String HEARTBEAT_URL      = "/heartbeat";

    private static final String PARAM_RETURN_URL   = "returnUrl";

    private Set<String>         excludeUrlPrefixes = new HashSet<String>();
    private Set<String>         excludeUrls        = new HashSet<String>();
    private UserService         userService;

    public void init(FilterConfig filterConfig) throws ServletException {
        this.excludeUrlPrefixes.add(STATIC_URL);
        this.excludeUrlPrefixes.add(ASSETS_URL);
        this.excludeUrlPrefixes.add(API_URL);
        this.excludeUrlPrefixes.add(PROFILE_URL);
        this.excludeUrlPrefixes.add(HEARTBEAT_URL);
        this.userService = WebApplicationContextUtils.getWebApplicationContext(filterConfig.getServletContext()).getBean(UserService.class);
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
                                                                                             ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String servletPath = httpRequest.getServletPath();
        UserContext userContext = UserContextUtils.getContext(httpRequest, this.userService);
        if (!LOGIN_URL.equalsIgnoreCase(servletPath) && !isExcludeURL(servletPath)) {
            if (userContext == null) {
                String returnUrl = servletPath;
                String queryString = httpRequest.getQueryString();
                if (StringUtils.isNotBlank(queryString)) {
                    returnUrl = returnUrl + "?" + queryString;
                }

                returnUrl = URLEncoder.encode(returnUrl, "UTF-8");

                ((HttpServletResponse) response).sendRedirect(httpRequest.getContextPath() + LOGIN_URL + "?"
                                                              + PARAM_RETURN_URL + "=" + returnUrl);
            } else {
                try {
                    UserContextHolder.setUserContext(userContext);

                    User user = this.userService.get(userContext.getId());
                    if (user.getTeamId() != null) {
                        chain.doFilter(request, response);
                    } else {
                        ((HttpServletResponse) response).sendRedirect(httpRequest.getContextPath()
                                                                      + "/profile?teamIdIsNull=true");
                    }
                } finally {
                    UserContextHolder.clear();
                }
            }
        } else {
            if (LOGIN_URL.equalsIgnoreCase(servletPath) && (userContext != null)) {
                try {
                    UserContextHolder.setUserContext(userContext);
                    ((HttpServletResponse) response).sendRedirect(httpRequest.getContextPath() + "/");
                } finally {
                    UserContextHolder.clear();
                }
            } else if (servletPath.startsWith(PROFILE_URL) && (userContext != null)) {
                try {
                    UserContextHolder.setUserContext(userContext);
                    chain.doFilter(httpRequest, response);
                } finally {
                    UserContextHolder.clear();
                }
            } else {
                chain.doFilter(httpRequest, response);
            }
        }
    }

    private boolean isExcludeURL(String servletPath) {
        for (String excludeUrlPrefix : this.excludeUrlPrefixes) {
            if (servletPath.startsWith(excludeUrlPrefix)) {
                return true;
            }
        }

        for (String excludeUrl : this.excludeUrls) {
            if (servletPath.equalsIgnoreCase(excludeUrl)) {
                return true;
            }
        }

        return false;
    }

    public void destroy() {
        // do nothing
    }

}
