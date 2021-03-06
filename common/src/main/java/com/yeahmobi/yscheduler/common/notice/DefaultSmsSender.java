package com.yeahmobi.yscheduler.common.notice;

import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

/**
 * @author Ryan Sun
 */
@Service
public class DefaultSmsSender implements SmsSender {

    private static final Log    LOGGER   = LogFactory.getLog(DefaultSmsSender.class);

    private static final String DEV_MODE = "yscheduler.devMode";

    private boolean             devMode  = true;

    private String              smsApi;

    private String              app;

    private String              appKey;

    public String getSmsApi() {
        return this.smsApi;
    }

    public void setSmsApi(String smsApi) {
        this.smsApi = smsApi;
    }

    public String getApp() {
        return this.app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getAppKey() {
        return this.appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public DefaultSmsSender() {
        String devModeStr = System.getProperty(DEV_MODE, "true");
        if ("false".equals(devModeStr)) {
            this.devMode = false;
        }
    }

    public void send(Message message) {
        LOGGER.info("Send message>>> subject:" + message.getSubject() + "; to:" + message.getTo());
        if (this.devMode) {
            return;
        }
        try {
            HttpClient client = new HttpClient();
            PostMethod postMethod = new PostMethod(this.smsApi);
            NameValuePair[] parameters = new NameValuePair[6];
            for (String phoneNumbers : message.getTo()) {
                if (StringUtils.isNotBlank(phoneNumbers)) {
                    for (String phoneNumberStr : StringUtils.split(phoneNumbers, ";")) {
                        if (StringUtils.isNotBlank(phoneNumberStr)) {
                            try {
                                String phoneNumber = StringUtils.trim(phoneNumberStr);
                                String time = String.valueOf(new Date().getTime() / 1000);
                                String sign = DigestUtils.md5Hex(this.app + this.appKey + time).substring(0, 8).toLowerCase();
                                parameters[0] = new NameValuePair("to", phoneNumber);
                                parameters[1] = new NameValuePair("text", message.getSubject());
                                parameters[2] = new NameValuePair("ip", " ");
                                parameters[3] = new NameValuePair("_app", "yscheduler");
                                parameters[4] = new NameValuePair("_time", time);
                                parameters[5] = new NameValuePair("_sign", sign);
                                postMethod.addParameters(parameters);
                                postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");

                                client.executeMethod(postMethod);

                                String response = postMethod.getResponseBodyAsString();

                                if (!response.contains("\"code\":0")) {
                                    LOGGER.warn("Send sms failed. " + response);
                                }
                            } catch (Throwable e) {
                                LOGGER.warn(e);
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            LOGGER.error(e, e);
        }
    }

    // public static void main(String[] args) {
    // DefaultSmsSender smsSender = new DefaultSmsSender();
    // smsSender.devMode = false;
    //
    // smsSender.smsApi = "http://ips.ymtech.info/notify/api/sms";
    // smsSender.app = "yscheduler";
    // smsSender.appKey = "6v8a#lo2";
    // Message msg = new Message();
    // msg.setContent("hi，说点什么");
    // msg.setSubject("有错误");
    // List<String> to = new ArrayList<String>();
    // to.add("15921096896");
    // msg.setTo(to);
    // smsSender.send(msg);
    // }
}
