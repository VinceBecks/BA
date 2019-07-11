Feature: Follow a user
  This feature file describes the behaviour of the REST-API for POST requests at the endpoint /api/users/{userId}/follower to follow a specified user.
  There should be follwoing behaviour at the REST-API:
  - If the request contains the header "Authorization" with a valid token of an user, then the http response status-code will be 204
  - If the requesting user is already a follower of the specified user, then the http response status-code will be 400
  - If the request doesn´t contain a valid token of an user, then the http response status-code will be 401
  - If the specified account to follow belongs to a moderator, then the http response body will contain an appropriate information about the mistake and the http response status-code will be 400
  - If the transmitted token belongs to a moderator, then the http response status-code will be 403
  - If the specified user to follow doesn´t exist, then the http response status-code will be 404

  Scenario: Follow a user
  Request to follow a user

    Given the user "max" is authenticated
    When a client sends a POST "/users/2/follower" request for user "max" to follow user john
    Then the client will receive the "NO_CONTENT" Status Code


  Scenario: Requesting user is already a follower of the specified user
  Each user can be just once a follower of a specified user

    Given the user "max" is authenticated
    And the user max is a follower of user john with id 2
    When a client sends a POST "/users/2/follower" request for user "max" to follow user john
    Then the client will receive the "BAD_REQUEST" Status Code
    And the HTTP response body contains following JSON of an error message:
      """
      {
        "errorMessage": "User is already a follower of the specified user"
      }
      """

  Scenario: Unauthorised request to follow a user
  The request to follow an user must contain a valid token of an user

    When a client sends a POST "/users/2/follower" request for user max without a valid token to follow user john
    Then the client will receive the "UNAUTHORIZED" Status Code


  Scenario: Account to follow belongs to a moderator
  The account to follow must be from an user

    Given the user "max" is authenticated
    When a client sends a POST "/users/4/follower" request for user "max" to follow the account of a moderator
    Then the client will receive the "BAD_REQUEST" Status Code
    And the HTTP response body contains following JSON of an error message:
      """
      {
        "errorMessage": "Specified account to follow belongs to a moderator"
      }
      """

  Scenario: Transmitted token from the request to follow a specified user belongs to a moderator
  Just users can follow users

    Given the moderator "werner" is authenticated
    When a client sends a POST "/users/2/follower" request for moderator "werner" to follow user john
    Then the client will receive the "FORBIDDEN" Status Code


  Scenario: The user to follow doesn´t exist
  The account from an user to follow must be existing

    Given the user "max" is authenticated
    And there is no user with id 9999
    When a client sends a POST "/users/9999/follower" request for user "max" to follow the specified account
    Then the client will receive the "NOT_FOUND" Status Code