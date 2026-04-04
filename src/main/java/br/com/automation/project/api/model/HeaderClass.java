package br.com.automation.project.api.model;

import com.sun.net.httpserver.Headers;

import java.util.Map;

public class HeaderClass {

    private static String token;
    private static Map<String, Object> headersCredParams;
    private static Headers headers;

    public static void setToken(String token) {
        HeaderClass.token = String.format("Bearer %s", token);
    }

    public static String getToken() {
        return HeaderClass.token;
    }

    public static void setHeadersCredParams(Map<String, Object> headersCredParams) {
        HeaderClass.headersCredParams = headersCredParams;
    }

    public static Map<String, Object> getHeadersCredParams() {
        return HeaderClass.headersCredParams;
    }

    public static void setHeaders(Headers headers) {
        HeaderClass.headers = headers;
    }

    public static Headers getHeaders() {
        return HeaderClass.headers;
    }
}
