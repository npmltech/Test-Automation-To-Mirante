package br.com.automation.project.api.model;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ResponseClass {

    private static String response;
    private static int httpStatusCode;
    private static Map<String, String> cookies;

    public static void setResponse(String response) {
        ResponseClass.response = response;
    }

    public static String getResponse() {
        return ResponseClass.response;
    }

    public static void setHttpStatusCode(int httpStatusCode) {
        ResponseClass.httpStatusCode = httpStatusCode;
    }

    public static int getHttpStatusCode() {
        return ResponseClass.httpStatusCode;
    }

    public static void setCookies(Map<String, String> cookies) {
        ResponseClass.cookies = Stream.of(cookies).map(Map::entrySet).flatMap(Collection::stream)
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static Map<String, String> getCookies() {
        return ResponseClass.cookies;
    }
}
