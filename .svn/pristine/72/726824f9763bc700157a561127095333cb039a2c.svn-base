package com.yeahmobi.yscheduler.agentframework.client;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.deserializer.ExtraProcessor;
import com.alibaba.fastjson.parser.deserializer.ExtraTypeProvider;
import com.yeahmobi.yscheduler.agentframework.AgentRequest;
import com.yeahmobi.yscheduler.agentframework.AgentResponse;
import com.yeahmobi.yscheduler.agentframework.AgentResponseCode;
import com.yeahmobi.yscheduler.agentframework.exception.AgentClientException;
import com.yeahmobi.yscheduler.common.Constants;

/**
 * @author Leo.Liang
 */
public class DefaultAgentClient implements AgentClient {

    private static final String EVENT_TYPE_PING = "ping";
    private int                 port            = Constants.DEFAULT_AGENT_PORT;
    private String              agentName;
    private int                 connectTimeout  = 1000;
    private int                 socketTimeout   = 3000;

    /**
     * @param connectTimeout the connectTimeout to set
     */
    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    /**
     * @param socketTimeout the socketTimeout to set
     */
    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public DefaultAgentClient(String agentName) {
        this(Constants.DEFAULT_AGENT_PORT, agentName);
    }

    public DefaultAgentClient(int port, String agentName) {
        this.port = port;
        this.agentName = agentName;
    }

    /**
     * @param agentName the agentName to set
     */
    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    private URI buildURI(String host, String eventType) throws URISyntaxException {
        return new URI(String.format("http://%s:%d/%s/?" + AgentRequest.REQKEY_EVENT_TYPE + "=" + eventType, host,
                                     this.port, this.agentName));
    }

    public <T> AgentResponse<T> call(String host, AgentRequest request) throws AgentClientException {
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put(AgentRequest.REQKEY_PARAMS, JSON.toJSONString(request.getParams()));
            return post(buildURI(host, request.getEventType()), params);
        } catch (Exception e) {
            throw new AgentClientException("Fail to call agent.", e);
        }
    }

    public boolean ping(String host) {
        try {
            AgentResponse<?> resp = call(host, new AgentRequest(EVENT_TYPE_PING, null));

            if ((resp != null) && AgentResponseCode.SUCCESS.equals(resp.getResponseCode())) {
                return true;
            }

            return false;

        } catch (Exception e) {
            return false;
        }

    }

    @SuppressWarnings("unchecked")
    private <T> AgentResponse<T> post(URI uri, Map<String, String> params) throws ClientProtocolException, IOException,
                                                                          AgentClientException {
        RequestBuilder requestBuilder = RequestBuilder.post().setUri(uri).setConfig(buildConfig());

        for (Map.Entry<String, String> entry : params.entrySet()) {
            requestBuilder.addParameter(entry.getKey(), entry.getValue());
        }

        HttpUriRequest uriRequest = requestBuilder.build();

        CloseableHttpClient httpclient = HttpClientBuilder.create().build();

        CloseableHttpResponse response = httpclient.execute(uriRequest);
        try {
            StatusLine statusLine = response.getStatusLine();
            if (HttpStatus.SC_OK == statusLine.getStatusCode()) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    return JSON.parseObject(IOUtils.toString(entity.getContent()), AgentResponse.class,
                                            new AgentResponseDataProcessor());
                } else {
                    throw new AgentClientException("No response data");
                }
            } else {
                throw new AgentClientException(String.format("Agent unavailable. (responseCode=%d, reason=%s)",
                                                             statusLine.getStatusCode(), statusLine.getReasonPhrase()));
            }

        } finally {
            httpclient.close();
        }
    }

    private static class AgentResponseDataProcessor implements ExtraProcessor, ExtraTypeProvider {

        @SuppressWarnings("unchecked")
        public void processExtra(Object object, String key, Object value) {
            if ((object instanceof AgentResponse) && AgentResponse.FIELD_RESPONSE_DATA.equals(key)) {
                AgentResponse resp = (AgentResponse) object;
                resp.setResponseData(value);
            }
        }

        public Type getExtraType(Object object, String key) {
            if ((object instanceof AgentResponse) && AgentResponse.FIELD_RESPONSE_DATA.equals(key)) {
                AgentResponse resp = (AgentResponse) object;
                try {
                    return Class.forName(resp.getResponseType());
                } catch (ClassNotFoundException e) {
                    return null;
                }
            }
            return null;
        }

    }

    private RequestConfig buildConfig() {
        return RequestConfig.custom().setConnectTimeout(this.connectTimeout).setSocketTimeout(this.socketTimeout).build();
    }

}
