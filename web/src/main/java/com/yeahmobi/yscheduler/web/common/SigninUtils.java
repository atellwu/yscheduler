package com.yeahmobi.yscheduler.web.common;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpStatus;

public class SigninUtils {

    private static final Log    LOGGER    = LogFactory.getLog(SigninUtils.class);

    private static final String signinApi = "https://op.ymtech.info/op/api/ad_isauth.ashx";

    private static final String key       = "31d860f7-6f7f-48d3-97b3-8407d5083f34";

    private static final String SUCCESS   = "{\"msg\":\"Sucesses\"}";

    private static final String FAIL      = "{\"msg\":\"用户验证失败！用户名或密码错误！\"}";

    public static boolean signin(String user, String password) {
        try {
            HttpClient client = new HttpClient();
            PostMethod postMethod = new PostMethod(signinApi);
            NameValuePair[] parameters = new NameValuePair[3];
            String base64EncodedUser = Base64.encodeBase64String(user.getBytes());
            String base64EncodedPassword = Base64.encodeBase64String(password.getBytes());
            String sign = DigestUtils.md5Hex(base64EncodedUser + base64EncodedPassword + key);

            parameters[0] = new NameValuePair("UserName", base64EncodedUser);
            parameters[1] = new NameValuePair("PassWord", base64EncodedPassword);
            parameters[2] = new NameValuePair("Sign", sign);
            postMethod.addParameters(parameters);
            postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");

            int returnCode = client.executeMethod(postMethod);
            String response = IOUtils.toString(postMethod.getResponseBodyAsStream());

            if (returnCode == HttpStatus.SC_OK) {
                if (SUCCESS.equals(response)) {
                    return true;
                } else if (FAIL.equals(response)) {
                    return false;
                } else {
                    // 正常情况下不会走到这个分支
                    LOGGER.error("Sign in failed: " + response);
                    throw new IllegalArgumentException(response);
                }
            } else {
                // sms api出问题。
                throw new IllegalArgumentException(response);
            }

        } catch (Exception e) {
            LOGGER.error(e, e);
            throw new IllegalArgumentException(e.getMessage());
        }

    }

}
