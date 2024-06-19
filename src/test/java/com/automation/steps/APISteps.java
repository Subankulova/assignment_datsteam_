package com.automation.steps;

import com.automation.utils.RestAssuredUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.Assert;

import java.io.File;
import java.io.FileNotFoundException;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static com.automation.utils.RestAssuredUtils.response;


public class APISteps {
    @Given("user set up request for {string} end point")
    public void user_set_up_request_for_end_point(String endpoint) {
        RestAssuredUtils.setEndPoint(endpoint);
    }

    @When("user performs GET request")
    public void user_perform_get_request() {
        RestAssuredUtils.get();
        System.out.println("Response Body: " + response.getBody().asString());

    }

    @When("user performs POST request")
    public void user_perform_post_request() {
        RestAssuredUtils.post();
    }

    @When("user performs PUT request")
    public void user_perform_put_request() {
        RestAssuredUtils.put();
    }

    @Then("verify status code is {int}")
    public void verify_status_code_is(int expectedStatusCode) {
        Assert.assertEquals(response.getStatusCode(),expectedStatusCode);
    }

    @And("set up parameters")
    public void setUpParameters(DataTable dataTable) {
        Map<String, String> params = dataTable.asMap(String.class, String.class);
        params.forEach((key, value) -> {
            RestAssuredUtils.setQueryParameter(key, value);
        });
    }

    @And("set header value {string} to {string}")
    public void setHeaderValueTo(String key, String value) {
        RestAssuredUtils.setHeader(key, value);
    }


    @And("set body for the create booking request")
    public void setBodyForTheCreateBookingRequest() throws FileNotFoundException {
        String body = readDataFromFile("/Users/subankulova/IdeaProjects/GetBookingApiTesting/src/test/resources/data/create_valid_body.json");
        System.out.println("Request Body: " + body);
        RestAssuredUtils.setBody(body);
    }

    public static String readDataFromFile(String filePath) throws FileNotFoundException {
        File file = new File(filePath);
        Scanner sc = new Scanner(file);
        sc.useDelimiter("\\Z");
        return sc.next();
    }

    @And("set body for the create token request")
    public void setBodyForTheCreateTokenRequest() throws FileNotFoundException {
        String body = readDataFromFile("/Users/subankulova/IdeaProjects/GetBookingApiTesting/src/test/resources/data/create_token_body.json");
        System.out.println("Request Body: " + body);
        RestAssuredUtils.setBody(body);
    }

    @And("verify response body has a token")
    public void verifyResponseBodyHasAToken() {
        String token = RestAssuredUtils.getResponseField("token");
        System.out.println("token is ===> " + token);
        Assert.assertFalse(token.isEmpty());
    }

    @And("verify booking exists with firstname {string} and lastname {string}")
    public void verifyBookingExistsWithFirstname(String expectedFirstname, String expectedLastname) {
        String actualFirstname = RestAssuredUtils.getResponseField("firstname");
        String actualLastname = RestAssuredUtils.getResponseField("lastname");

        // Отладочный вывод для проверки полученных значений
        System.out.println("Actual firstname: " + actualFirstname);
        System.out.println("Actual lastname: " + actualLastname);

        Assert.assertEquals(actualFirstname, expectedFirstname);
        Assert.assertEquals(actualLastname, expectedLastname);
    }

    @And("verify response body contains error message {string}")
    public void verifyResponseBodyContainsErrorMessage(String errorMessage) {
        Assert.assertTrue(response.getBody().asString().contains(errorMessage));
    }

    @When("user performs GET request with url id = {string}")
    public void userPerformsGETRequestWithUrlId(String id) {
        response = RestAssured.get("/booking/{id}", id);

    }


    private static class BookingId {
        private int bookingId;

        public int getBookingId() {
            return bookingId;
        }
    }
    private Gson gson = new Gson();

    @And("verify response body has a data")
    public void verifyResponseBodyHasAData() {
        String responseBody = response.getBody().asString();
        Assert.assertFalse(responseBody.isEmpty(), "Response body is empty");
        Assert.assertTrue(responseBody.contains("bookingId"), "Response does not contain booking data");

        // Преобразование JSON в список объектов с полем bookingid
        Type listType = new TypeToken<List<BookingId>>() {}.getType();
        List<BookingId> bookingIds = gson.fromJson(responseBody, listType);

        // Проверка наличия хотя бы одного объекта с bookingid
        boolean hasBookingId = false;
        for (BookingId bookingId : bookingIds) {
            if (bookingId.getBookingId() > 0) {
                hasBookingId = true;
                break;
            }
        }

        Assert.assertTrue(hasBookingId, "Response does not contain booking data");
    }

    @And("set body with invalid {string} for the create booking request")
    public void setBodyWithInvalidForTheCreateBookingRequest(String firstname) {
        String body = String.format("{\"firstname\":\"%s\",\"lastname\":\"Doe\",\"totalprice\":123,\"depositpaid\":true,\"bookingdates\":{\"checkin\":\"2023-01-01\",\"checkout\":\"2023-01-02\"},\"additionalneeds\":\"Breakfast\"}", firstname);
        System.out.println("Request Body: " + body);
        RestAssuredUtils.setBody(body);
    }

    @And("verify response body has error message {string}")
    public void verifyResponseBodyHasErrorMessage(String errorMessage) {
        String responseBody = response.getBody().asString();
        Assert.assertTrue(responseBody.contains(errorMessage), "Error message is not found in the response");
    }

    @And("set body with invalid dates for the create booking request")
    public void setBodyWithInvalidDatesForTheCreateBookingRequest() throws FileNotFoundException {
        String body = readDataFromFile("/Users/subankulova/IdeaProjects/GetBookingApiTesting/src/test/resources/data/create_invaliddate_body.json");
        System.out.println("Request Body: " + body);
        RestAssuredUtils.setBody(body);
    }

    @Then("verify status code is {string}")
    public void verifyStatusCodeIs(String expectedStatusCode) {
        int statusCode = Integer.parseInt(expectedStatusCode);
        int actualStatusCode = RestAssuredUtils.getStatusCode();
        Assert.assertEquals(actualStatusCode, statusCode, "Status code mismatch!");
    }
}





