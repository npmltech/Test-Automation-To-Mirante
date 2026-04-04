package br.com.automation.project.api.model;

import java.util.Map;

public class BodyParamsClass {

    private static Map<String, Object> bodyParams;

    public static void setBodyParams(Map<String, Object> bodyParams) {
        BodyParamsClass.bodyParams = bodyParams;
    }

    public static Map<String, Object> getBodyParams() {
        return BodyParamsClass.bodyParams;
    }
}
