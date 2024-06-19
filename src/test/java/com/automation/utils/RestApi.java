package com.automation.utils;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class RestApi {
    public static void main(String[] args) throws FileNotFoundException {
        String body = readDataFromFile("/Users/subankulova/IdeaProjects/TestNG_Cucumber/src/test/resources/data/create_valid_body.json");
        //create a blank container to store request
        RequestSpecification reqSpecs = RestAssured.given();
        //set up or create a request
        reqSpecs = reqSpecs.baseUri("https://restful-booker.herokuapp.com");
        reqSpecs = reqSpecs.body(body);
        reqSpecs = reqSpecs.header("Content-Type","application/json");
        //send request and store response
        Response response = reqSpecs.post("/booking");
        System.out.println(response.getStatusCode());
        System.out.println("Response Body: " + response.getBody().asString());
    }
    public static String readDataFromFile(String filePath) throws FileNotFoundException {
        File file = new File(filePath);
        Scanner sc = new Scanner(file);
        sc.useDelimiter("\\Z");
        return sc.next();
    }
}
