package com.yeahmobi.yscheduler.agentframework.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

public class HttpClientUtil {

    private static final int                 CONN_POOL_SIZE          = 10;
    private static final int                 DEFAULT_CONNECT_TIMEOUT = 3000;
    private static final int                 DEFAULT_SOCKET_TIMEOUT  = 10000;

    private static final CloseableHttpClient httpclient;

    static {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
        connManager.setMaxTotal(CONN_POOL_SIZE);
        SocketConfig defaultSocketConfig = SocketConfig.custom().setSoTimeout(DEFAULT_SOCKET_TIMEOUT).build();
        connManager.setDefaultSocketConfig(defaultSocketConfig);
        httpClientBuilder.setConnectionManager(connManager);

        httpclient = httpClientBuilder.build();
    }

    public static String get(String uri, Map<String, String> params) throws ClientProtocolException, IOException {
        return get(uri, params, DEFAULT_CONNECT_TIMEOUT, DEFAULT_SOCKET_TIMEOUT);
    }

    public static CloseableHttpResponse getResponse(String uri, Map<String, String> params)
                                                                                           throws ClientProtocolException,
                                                                                           IOException {
        return getResponse(uri, params, DEFAULT_CONNECT_TIMEOUT, DEFAULT_SOCKET_TIMEOUT);
    }

    public static String get(String uri, Map<String, String> params, int connectTimeout, int socketTimeout)
                                                                                                           throws ClientProtocolException,
                                                                                                           IOException {

        CloseableHttpResponse response = getResponse(uri, params, connectTimeout, socketTimeout);

        try {
            String result = getContent(response);
            return result;

        } finally {
            response.close();
        }

    }

    private static String getContent(CloseableHttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();

        InputStream ins = entity.getContent();
        String result = IOUtils.toString(ins, "UTF-8");
        return result;
    }

    public static CloseableHttpResponse getResponse(String uri, Map<String, String> params, int connectTimeout,
                                                    int socketTimeout) throws ClientProtocolException, IOException {

        RequestBuilder requestBuilder = RequestBuilder.get().setUri(uri).setConfig(buildConfig(connectTimeout,
                                                                                               socketTimeout));

        for (Map.Entry<String, String> entry : params.entrySet()) {
            requestBuilder.addParameter(entry.getKey(), entry.getValue());
        }

        HttpUriRequest uriRequest = requestBuilder.build();

        CloseableHttpResponse response = httpclient.execute(uriRequest);
        StatusLine statusLine = response.getStatusLine();
        if (HttpStatus.SC_OK != statusLine.getStatusCode()) {
            String result = getContent(response);
            response.close();
            throw new IOException(String.format("Call Uri(%s) error: responseCode=%d, content=%s", uri,
                                                statusLine.getStatusCode(), StringUtils.abbreviate(result, 128)));
        }

        return response;

    }

    private static RequestConfig buildConfig(int connectTimeout, int socketTimeout) {
        return RequestConfig.custom().setConnectTimeout(connectTimeout).setSocketTimeout(socketTimeout).build();
    }

}
