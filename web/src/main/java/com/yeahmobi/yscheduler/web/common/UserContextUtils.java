package com.yeahmobi.yscheduler.web.common;

import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.yeahmobi.yscheduler.common.EncryptUtils;
import com.yeahmobi.yscheduler.common.PasswordEncoder;
import com.yeahmobi.yscheduler.model.User;
import com.yeahmobi.yscheduler.model.common.UserContext;
import com.yeahmobi.yscheduler.model.common.UserContextHolder;
import com.yeahmobi.yscheduler.model.service.UserService;

/**
 * @author Leo.Liang
 */
public class UserContextUtils {

    private static final String USER_CONTEXT_COOKIE_KEY = "_uc";
    private static final String ENCRYPT_KEY             = "leo.liang";
    private static final int    COOKIE_AGE              = 60 * 60 * 24 * 30;

    public static void setContext(HttpServletResponse resp, UserContext userContext) throws Exception {
        String password = userContext.getPassword();
        userContext.setPassword(PasswordEncoder.encode(password));

        Cookie cookie = new Cookie(USER_CONTEXT_COOKIE_KEY, EncryptUtils.encode(ENCRYPT_KEY, userContext.toString()));
        cookie.setPath("/");
        cookie.setMaxAge(COOKIE_AGE);
        resp.addCookie(cookie);
    }

    public static void removeContext(HttpServletResponse resp) {
        Cookie cookie = new Cookie(USER_CONTEXT_COOKIE_KEY, null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        resp.addCookie(cookie);
        UserContextHolder.clear();
    }

    public static UserContext getContext(HttpServletRequest req, UserService userService) {
        try {
            Cookie[] cookies = req.getCookies();

            for (Cookie cookie : cookies) {
                if (USER_CONTEXT_COOKIE_KEY.equals(cookie.getName())) {
                    String value = cookie.getValue();
                    if (StringUtils.isNotBlank(value)) {
                        UserContext userContext = UserContext.valueOf(EncryptUtils.decode(ENCRYPT_KEY, value));

                        if ((userContext != null) && new Date().before(userContext.getExpireDate())) {
                            User user = userService.get(userContext.getId());
                            if ((user != null)
                                && PasswordEncoder.encode(user.getPassword()).equals(userContext.getPassword())) {
                                return userContext;
                            }
                        }
                    }

                    break;
                }
            }
        } catch (Exception e) {
            // ignore
        }

        return null;
    }
}
