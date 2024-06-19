package com.automation.steps;

import com.automation.utils.ConfigReader;
import io.cucumber.java.Before;
import io.restassured.RestAssured;

public class Hooks {
    @Before("@api")
    public void setUpApi(){
        ConfigReader.initProperties();
        RestAssured.baseURI = ConfigReader.getProperty("api.url");}
}
