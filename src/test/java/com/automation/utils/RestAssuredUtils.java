package com.automation.utils;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.List;

public class RestAssuredUtils {
    static RequestSpecification reqSpec = RestAssured.given();
    static String endpoint;
    public static Response response;

    public static void setEndPoint(String endpoint) {
        RestAssuredUtils.endpoint = endpoint;
    }

    public static void setBody(String body) {
        reqSpec = reqSpec.body(body);
    }

    public static void setHeader(String key, String value) {
        reqSpec = reqSpec.header(key, value);
    }


    public static void get() {
        response = reqSpec.log().all().get(endpoint);
    }

    public static void post() {
        response = reqSpec.log().all().post(endpoint);
    }

    public static void put() {
        response = reqSpec.log().all().put(endpoint);
    }

    public static int getStatusCode() {
        return response.getStatusCode();
    }

    public static String getResponseField(String path) {
        Object fieldValue = JsonPath.from(response.asString()).get(path);
        if (fieldValue instanceof List) {
             List<String> listValue = (List<String>) fieldValue;
            return listValue.isEmpty() ? null : listValue.get(0);
        } else {
             return String.valueOf(fieldValue);
        }
    }


    public static void setQueryParameter(String key, String value) {
        reqSpec = reqSpec.params(key, value);
    }
}