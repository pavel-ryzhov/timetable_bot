package org.example.utils;

import okhttp3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.Collections;
import java.util.Map;

public final class RequestsService {

    private static RequestsService requestsService = null;
    private static final Logger LOGGER = LogManager.getLogger(RequestsService.class);

    private final OkHttpClient client;

    public static RequestsService getInstance() {
        if (requestsService == null) {
            requestsService = new RequestsService();
        }
        return requestsService;
    }

    private RequestsService() {
        var cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        client = new OkHttpClient.Builder().cookieJar(new JavaNetCookieJar(cookieManager)).build();
    }

    public Response executePostRequest(String url, Map<String, String> headers, Map<String, String> dataParts) throws IOException {
        LOGGER.info("Executing POST request: " + url);
        var requestBuilder = new Request.Builder().url(url);
        for (var entry : headers.entrySet())
            requestBuilder.addHeader(entry.getKey(), entry.getValue());
        var multipartBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (var entry : dataParts.entrySet())
            multipartBody.addFormDataPart(entry.getKey(), entry.getValue());
        requestBuilder.post(multipartBody.build());
        return client.newCall(requestBuilder.build()).execute();
    }

    public Response executeGetRequest(String url, Map<String, String> headers) throws IOException {
        LOGGER.info("Executing GET request: " + url);
        var requestBuilder = new Request.Builder().url(url);
        for (var entry : headers.entrySet())
            requestBuilder.addHeader(entry.getKey(), entry.getValue());
        return client.newCall(requestBuilder.build()).execute();
    }

    public Response executeGetRequest(String url) throws IOException {
        return executeGetRequest(url, Collections.emptyMap());
    }
}
