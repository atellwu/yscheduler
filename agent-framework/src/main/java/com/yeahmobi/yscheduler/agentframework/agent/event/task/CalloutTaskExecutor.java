package com.yeahmobi.yscheduler.agentframework.agent.event.task;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yeahmobi.yscheduler.agentframework.agent.event.task.CalloutTaskHolder.Pair;
import com.yeahmobi.yscheduler.agentframework.agent.task.AgentTask;
import com.yeahmobi.yscheduler.agentframework.agent.task.BaseTaskExecutor;
import com.yeahmobi.yscheduler.agentframework.agent.task.TaskTransaction;
import com.yeahmobi.yscheduler.agentframework.agent.task.TaskTransaction.Context;
import com.yeahmobi.yscheduler.agentframework.exception.AgentClientException;

/**
 * 发起callout：http://localhost:24368/yscheduler/?eventType=TASK_HTTP_CALLOUT&params={
 * 'calloutUrl':'http://www.baidu.com','needCallback':'true','agentHost':'localhost:24368'}<br>
 * 查看callout状态：http://localhost:24368/yscheduler/?eventType=TASK_STATUS&params={%22txId%22:%2221%22} <br>
 * 查看callout日志：http://localhost:24368/yscheduler/?eventType=TASK_LOG&params={%22txId%22:%2221%22} <br>
 *
 * @author atell.wu
 */
public class CalloutTaskExecutor<T extends AgentTask> extends BaseTaskExecutor<T> {

    private static final String     CALLOUT_DONE         = "callout_done";

    private static final Logger     LOGGER               = LoggerFactory.getLogger(CalloutTaskExecutor.class);

    private static final int        INTERVAL             = 1;
    // 等待6小时，即6*60*60s
    private static final long       MAX_WAIT_COUNT       = 21600;
    // 超时的返回值
    private static final Integer    RETURN_VALUE_TIMEOUT = -2;

    private volatile boolean        cancel               = false;

    private int                     connectTimeout       = 3000;
    private int                     socketTimeout        = 10000;

    private String                  calloutUri;
    private HashMap<String, String> calloutParams        = new HashMap<String, String>();
    private String                  agentHost;

    private String                  cancelUri;
    private Map<String, String>     cancelParams         = new HashMap<String, String>();
    private boolean                 needCallback;

    public Integer execute(TaskTransaction taskTransaction) {
        Map<String, String> params = taskTransaction.getTask().getTaskParams();

        validate(params);

        Integer ret = null;

        try {
            Context context = taskTransaction.getContext();
            if (!context.containsKey(CALLOUT_DONE)) {
                // callout
                callout(taskTransaction, params);
                context.put(CALLOUT_DONE, true);
                taskTransaction.persistContext();
            }

            if (needCallback(params)) {
                // add futureTask to Map
                CalloutFutureTask task = new CalloutFutureTask();

                CalloutTaskHolder.put(taskTransaction.getId(), new Pair(taskTransaction, task));

                // wait for callback
                long waitCount = 0;
                while (!this.cancel) {
                    try {
                        ret = task.get(INTERVAL, TimeUnit.SECONDS);
                        break;

                        // 由callback设置taskTransaction成功与否
                        // taskTransaction.endWithSuccess(0);
                    } catch (TimeoutException e) {
                        // 30秒打一次log
                        if ((waitCount++ % 30) == 0) {
                            taskTransaction.info("Waiting for callback count: " + waitCount);
                        }
                        // 等待超过一定次数则自动失败
                        if (waitCount > MAX_WAIT_COUNT) {// 等待超过阈值后，直接fail
                            taskTransaction.info("Wait times reach limit and it will auto failed, count: " + waitCount);
                            ret = RETURN_VALUE_TIMEOUT;
                            break;
                        }
                    } catch (Throwable e) {
                        taskTransaction.error(e.getMessage(), e);
                        ret = 1;
                        break;
                    }
                }

            }

        } catch (Exception e1) {
            LOGGER.error(e1.getMessage(), e1);
            taskTransaction.error(e1.getMessage(), e1);
            ret = 1;
        }

        return ret;
    }

    public void cancel(TaskTransaction taskTransaction) throws Exception {
        taskTransaction.info("Cancelling.");

        if (this.cancelUri != null) {
            // 如果params中有cancelUrl，则调用，并等待200结果。否则直接返回
            this.cancelParams.put("txId", String.valueOf(taskTransaction.getId()));
            taskTransaction.info("Calling out for cancel, uri: " + this.cancelUri + ", params: " + this.cancelParams);
            StatusLine statusLine = get(this.cancelUri, this.cancelParams);
            taskTransaction.info("Calling out for cancel done, remote return statusLine:" + statusLine);
        }
        this.cancel = true;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    private void callout(TaskTransaction taskTransaction, Map<String, String> params) throws Exception {

        this.calloutParams.put("txId", String.valueOf(taskTransaction.getId()));
        String workflowName = params.get("workflowName");
        if (workflowName != null) {
            this.calloutParams.put("workflowName", workflowName);
        }
        String taskName = params.get("taskName");
        if (taskName != null) {
            this.calloutParams.put("taskName", taskName);
        }
        if (this.needCallback) {
            this.calloutParams.put("callback",
                                   "http://" + this.agentHost + "/yscheduler/?eventType=TASK_CALLBACK&txId="
                                           + String.valueOf(taskTransaction.getId()) + "&returnValue=");
        }

        taskTransaction.info("Calling out, uri: " + this.calloutUri + ", params: " + this.calloutParams);
        StatusLine statusLine = get(this.calloutUri, this.calloutParams);
        taskTransaction.info("Call out done, remote return statusLine:" + statusLine);
    }

    private void parseQuery(Map<String, String> calloutParams, String[] splits) {
        if (splits.length > 1) {
            String[] paramSplits = StringUtils.split(splits[1], '&');
            for (String keyValue : paramSplits) {
                String[] keyValueSplits = StringUtils.split(keyValue, '=');
                Validate.isTrue(keyValueSplits.length == 2, "query'" + splits + "' invalid");
                String key = keyValueSplits[0];
                String value = keyValueSplits[1];
                calloutParams.put(key, value);
            }
        }
    }

    private void validate(Map<String, String> params) throws IllegalArgumentException {
        // callout
        String calloutUrl = params.get("calloutUrl");
        Validate.notEmpty(calloutUrl, "calloutUrl cannot be empty");
        String[] splits = StringUtils.split(calloutUrl, '?');
        this.calloutUri = splits[0];
        parseQuery(this.calloutParams, splits);
        this.needCallback = BooleanUtils.toBoolean(params.get("needCallback"));
        this.agentHost = params.get("agentHost");
        if (this.needCallback) {
            Validate.notEmpty(this.agentHost, "agentHost cannot be empty since needCallback");
        }
        // cancel
        String cancelUrl = params.get("cancelUrl");
        if (StringUtils.isNotEmpty(cancelUrl)) {
            splits = StringUtils.split(cancelUrl, '?');
            this.cancelUri = splits[0];
            parseQuery(this.cancelParams, splits);
        }
    }

    private StatusLine get(String uri, Map<String, String> params) throws ClientProtocolException, IOException,
                                                                  AgentClientException {

        RequestBuilder requestBuilder = RequestBuilder.get().setUri(uri).setConfig(buildConfig());

        for (Map.Entry<String, String> entry : params.entrySet()) {
            requestBuilder.addParameter(entry.getKey(), entry.getValue());
        }

        HttpUriRequest uriRequest = requestBuilder.build();

        CloseableHttpClient httpclient = HttpClientBuilder.create().build();

        CloseableHttpResponse response = httpclient.execute(uriRequest);
        try {
            StatusLine statusLine = response.getStatusLine();
            if (HttpStatus.SC_OK != statusLine.getStatusCode()) {
                throw new AgentClientException(String.format("Call Uri(%s) error: responseCode=%d, reason=%s", uri,
                                                             statusLine.getStatusCode(), statusLine.getReasonPhrase()));
            }

            return statusLine;

        } finally {
            httpclient.close();
        }

    }

    private RequestConfig buildConfig() {
        return RequestConfig.custom().setConnectTimeout(this.connectTimeout).setSocketTimeout(this.socketTimeout).build();
    }

    private boolean needCallback(Map<String, String> params) {
        return BooleanUtils.toBoolean(params.get("needCallback"));
    }

    public static class CalloutFutureTask extends FutureTask<Integer> {

        public CalloutFutureTask() {
            super(new Runnable() {

                public void run() {
                }
            }, null);
        }

        @Override
        public void set(Integer v) {
            super.set(v);
        }

    }

}
