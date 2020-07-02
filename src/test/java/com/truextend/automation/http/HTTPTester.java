package com.truextend.automation.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class HTTPTester {

    ConcurrentHashMap<String, String> headers = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, String> queryParams = new ConcurrentHashMap<>();
    String protocol;
    String host;
    Integer portNumber = null;
    String baseUrl;

    CloseableHttpClient httpClient = HttpClients.createDefault();
    RequestConfig requestConfig = RequestConfig.custom()
            .setSocketTimeout(30000)
            .setConnectTimeout(30000)
            .build();

    public HTTPTester() {

    }

    public HTTPTester(URL url) {
        protocol = url.getProtocol();
        host = url.getHost();
        if (url.getPath().length() > 1) {
            baseUrl = url.getPath().substring(1);
        }
        int port = url.getPort();
        if (port > 1) {
            portNumber = port;
        }
    }

    public HTTPTester(String a) throws MalformedURLException {
        this(new URL(a));
    }

    public HTTPTester(String protocol, String host) {
        this.protocol = protocol;
        this.host = host;
    }


    public HTTPTester(String protocol, String host, Integer portNumber) {
        this.protocol = protocol;
        this.host = host;
        this.portNumber = portNumber;
    }

    public void addHeader(String headerName, String headerValue) {
        headers.put(headerName.toLowerCase(), headerValue);
    }

    public void removeHeader(String headerName) {
        headers.remove(headerName.toLowerCase());
    }

    public void addHeaders(Map<String, String> newHeaders) {
        for (Map.Entry<String, String> h: newHeaders.entrySet()) {
            addHeader(h.getKey(), h.getValue());
        }
    }

    public void addQueryParam(String queryParam, String queryParamValue) {
        queryParams.put(queryParam, queryParamValue);
    }

    public void addQueryParams(Map<String, String> newHeaders) {
        queryParams.putAll(newHeaders);
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPortNumber(Integer portNumber) {
        this.portNumber = portNumber;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public HTTPResponse get(String url) throws IOException {
        HttpGet request = new HttpGet(buildUrl(url));
        addAllHeaders(request);
        return getHttpResponse(request);
    }

    public HTTPResponse delete(String url) throws IOException {
        HttpDelete request = new HttpDelete(buildUrl(url));
        addAllHeaders(request);
        return getHttpResponse(request);
    }

    public HTTPResponse postForm(String url, Map<String, Object> formParams) throws IOException {
        HttpPost request = new HttpPost(buildUrl(url));
        addHeader("Content-Type", "application/x-www-form-urlencoded");
        addAllHeaders(request);
        List<NameValuePair> urlParameters = new ArrayList<>();
        for (Map.Entry<String, Object> formParam: formParams.entrySet()) {
            urlParameters.add(new BasicNameValuePair(formParam.getKey(), formParam.getValue() != null ? formParam.getValue().toString(): null));
        }
        request.setEntity(new UrlEncodedFormEntity(urlParameters));
        return getHttpResponse(request);
    }

    public HTTPResponse postJson(String url, Map<String, Object> json) throws IOException {
        HttpPost request = new HttpPost(buildUrl(url));
        addHeader("Content-Type", "application/json");
        addAllHeaders(request);
        ObjectMapper objectMapper = new ObjectMapper();
        request.setEntity(new StringEntity(objectMapper.writeValueAsString(json)));
        return getHttpResponse(request);
    }

    public HTTPResponse postJson(String url, List json) throws IOException {
        HttpPost request = new HttpPost(buildUrl(url));
        addAllHeaders(request);
        ObjectMapper objectMapper = new ObjectMapper();
        request.setEntity(new StringEntity(objectMapper.writeValueAsString(json)));
        return getHttpResponse(request);
    }

    public HTTPResponse post(String url, String body) throws IOException {
        HttpPost request = new HttpPost(buildUrl(url));
        addAllHeaders(request);
        request.setEntity(new StringEntity(body));
        return getHttpResponse(request);
    }

    public HTTPResponse putForm(String url, Map<String, Object> formParams) throws IOException {
        HttpPut request = new HttpPut(buildUrl(url));
        addHeader("Content-Type", "application/x-www-form-urlencoded");
        addAllHeaders(request);
        List<NameValuePair> urlParameters = new ArrayList<>();
        for (Map.Entry<String, Object> formParam: formParams.entrySet()) {
            urlParameters.add(new BasicNameValuePair(formParam.getKey(), formParam.getValue() != null ? formParam.getValue().toString(): null));
        }
        request.setEntity(new UrlEncodedFormEntity(urlParameters));
        return getHttpResponse(request);
    }

    public HTTPResponse putJson(String url, Map<String, Object> json) throws IOException {
        HttpPut request = new HttpPut(buildUrl(url));
        addHeader("Content-Type", "application/json");
        addAllHeaders(request);
        ObjectMapper objectMapper = new ObjectMapper();
        request.setEntity(new StringEntity(objectMapper.writeValueAsString(json)));
        return getHttpResponse(request);
    }

    public HTTPResponse putJson(String url, List json) throws IOException {
        HttpPut request = new HttpPut(buildUrl(url));
        addAllHeaders(request);
        ObjectMapper objectMapper = new ObjectMapper();
        request.setEntity(new StringEntity(objectMapper.writeValueAsString(json)));
        return getHttpResponse(request);
    }

    public HTTPResponse put(String url, String body) throws IOException {
        HttpPut request = new HttpPut(buildUrl(url));
        addAllHeaders(request);
        if (body != null) {
            request.setEntity(new StringEntity(body));
        }
        return getHttpResponse(request);
    }

    public HTTPResponse patchForm(String url, Map<String, Object> formParams) throws IOException {
        HttpPatch request = new HttpPatch(buildUrl(url));
        addHeader("Content-Type", "application/x-www-form-urlencoded");
        addAllHeaders(request);
        List<NameValuePair> urlParameters = new ArrayList<>();
        for (Map.Entry<String, Object> formParam: formParams.entrySet()) {
            urlParameters.add(new BasicNameValuePair(formParam.getKey(), formParam.getValue() != null ? formParam.getValue().toString(): null));
        }
        request.setEntity(new UrlEncodedFormEntity(urlParameters));
        return getHttpResponse(request);
    }

    public HTTPResponse patchJson(String url, Map<String, Object> json) throws IOException {
        HttpPatch request = new HttpPatch(buildUrl(url));
        addHeader("Content-Type", "application/json");
        addAllHeaders(request);
        ObjectMapper objectMapper = new ObjectMapper();
        request.setEntity(new StringEntity(objectMapper.writeValueAsString(json)));
        return getHttpResponse(request);
    }

    public HTTPResponse patchJson(String url, List json) throws IOException {
        HttpPatch request = new HttpPatch(buildUrl(url));
        addAllHeaders(request);
        ObjectMapper objectMapper = new ObjectMapper();
        request.setEntity(new StringEntity(objectMapper.writeValueAsString(json)));
        return getHttpResponse(request);
    }

    public HTTPResponse patch(String url, String body) throws IOException {
        HttpPatch request = new HttpPatch(buildUrl(url));
        addAllHeaders(request);
        request.setEntity(new StringEntity(body));
        return getHttpResponse(request);
    }

    private void addAllHeaders(HttpUriRequest request) {
        for (Map.Entry<String, String> headers : headers.entrySet()) {
            request.addHeader(headers.getKey(), headers.getValue());
        }
    }

    private HTTPResponse getHttpResponse(HttpRequestBase request) throws IOException {
        request.setConfig(requestConfig);
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            System.out.println();
            System.out.println(request.getMethod() + " " + request.getURI());
            HTTPResponse httpResponse = new HTTPResponse();
            httpResponse.setCode(response.getStatusLine().getStatusCode());
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                httpResponse.setBody(EntityUtils.toByteArray(entity));
            } else {

                httpResponse.setBody(null);
            }
            System.out.println(httpResponse.getBodyAsString());
            return httpResponse;
        }
    }

    public void close() {
        try {
            httpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String buildUrl(String url) {
        StringBuilder sb = new StringBuilder();
        sb.append(protocol);
        sb.append("://");
        sb.append(host);
        if (portNumber != null) {
            sb.append(":");
            sb.append(portNumber);
        }
        if (StringUtils.isNotEmpty(baseUrl)) {
            sb.append("/");
            sb.append(baseUrl);
        }
        if (StringUtils.isNotEmpty(url)) {
            sb.append(url);
        }
        String queryParameters = buildQueryParams();
        if (StringUtils.isNotEmpty(queryParameters)) {
            sb.append("?");
            sb.append(queryParameters);
        }
        return sb.toString();
    }

    private String buildQueryParams() {
        StringBuilder sb = new StringBuilder();
        int i=0;
        for (Map.Entry<String, String> queryParam : queryParams.entrySet()) {
            sb.append(queryParam.getKey());
            sb.append("=");
            try {
                sb.append(URLEncoder.encode(queryParam.getValue(), "UTF-8").replace("+","%20"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (i<queryParams.size()-1) {
                sb.append("&");
            }
        }
        return sb.toString();
    }


}