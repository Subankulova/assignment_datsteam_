@api
Feature: Validate the functionality get booking api

  Scenario: Verify user can create token
    Given user set up request for "/auth" end point
    And set header value "Content-Type" to "application/json"
    And set body for the create token request
    When user performs POST request
    Then verify status code is 200
    And verify response body has a token

  Scenario: Verify user can create a booking
    Given user set up request for "/booking" end point
    And set header value "Content-Type" to "application/json"
    And set body for the create booking request
    When user performs POST request
    Then verify status code is 200

  Scenario: Create a booking with invalid dates
    Given user set up request for "/booking" end point
    And set header value "Content-Type" to "application/json"
    And set body with invalid dates for the create booking request
    When user performs POST request
    Then verify status code is 400
    And verify response body has error message "Invalid checkin or checkout date"

  Scenario Outline: Verify user cannot create a booking with invalid datas in firstname
    Given user set up request for "/booking" end point
    And set header value "Content-Type" to "application/json"
    And set body with invalid "<firstname>" for the create booking request
    When user performs POST request
    Then verify status code is "<statusCode>"
    And verify response body has error message "<errorMessage>"
    Examples:
      | firstname                                                            | statusCode | errorMessage      |
      |                                                                      | 400        | Invalid firstname |
      | !@#$%^&*(                                                            | 400        | Invalid firstname |
      | 12345678                                                             | 400        | Invalid firstname |
      | Wwecjndjncjnjvfnjvbsdjvbjdfnbvjdbvjdbvjhdsbvjhsdbvchjsdbvhjsbvjsdbvj | 400        | Invalid firstname |



  Scenario: Verify user can retrieve list of booking
    Given user set up request for "/booking" end point
    And set header value "Accept" to "application/json"
    When user performs GET request
    Then verify status code is 200
    And verify response body has a data

  Scenario: Get non-existent booking
    Given user set up request for "/booking/" end point
    And set header value "Accept" to "application/json"
    When user performs GET request with url id = "0000"
    Then verify status code is 404
    And verify response body contains error message "Not Found"

  Scenario: Verify user can filter Booking By Date
    Given user set up request for "/booking" end point
    And set up parameters
      | checkin    | 2013-02-23 |
      | checkout   | 2014-10-23 |
    When user performs GET request
    Then verify status code is 200
    And verify response body has a data

  Scenario: Verify user can filter Booking By Name
    Given user set up request for "/booking" end point
    And set up parameters
      | firstname | Chusi |
      | lastname  | Sub   |
    When user performs GET request
    Then verify status code is 200
    And verify response body has a data

