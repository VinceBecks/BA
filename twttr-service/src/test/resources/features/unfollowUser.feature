Feature: Unfollow a user
  This feature file describes the behaviour of the system for DELETE requests at the endpoint on /api/users/{userId}/follower to unfollow a specified user.
  There should be follwoing behaviour at the system:
  - If the request contains the header "Authorization" with a valid token of an user, then the http response status-code will be 204
  - If the request doesn´t contain a valid token, then, if the requesting user was a follower of the specified user, he still is a follower of the specified user and the http response status- will be 401
  - If the request contains a valid token which belongs to a moderator, then the http response status-code will be 403
  - If the specified account belongs to a moderator, then the http response body contains an appropriate information about the mistake and the http response status- will be 400
  - If the specified user doesn´t exist, then the http response status-code will be 404

  Scenario: Unfollow a user
  Request to unfollow a specified user

    Given the user "max" is authenticated
    And the user max is a follower of user john with id 2
    When a client sends a DELETE "/users/2/follower" request for user "max" to unfollow user john
    Then the HTTP response status-code will be 204

  Scenario: The requesting user isn´t a follower of the specified user
  The requesting user must be a follower of the specified user to unfollow him

    Given the user "max" is authenticated
    And the user max isn´t a follower of user john with id 2
    When a client sends a DELETE "/users/2/follower" request for user "max" to unfollow user john
    Then the HTTP response status-code will be 400
    And the HTTP response body contains following JSON of an error message:
      """
      {
        "errorMessage": "Requesting user isn´t a follower of the specified user"
      }
      """


  Scenario: Unauthorised request to unfollow a user
  The request must contain a valid token of an user

    When a client sends a DELETE "/users/2/follower" request without a valid token to unfollow user john
    Then the HTTP response status-code will be 401


  Scenario: Token from request to unfollow a user belongs to a moderator
  Account must be from an user

    Given the moderator "werner" is authenticated
    When a client sends a DELETE "/users/2/follower" request for moderator "werner" to unfollow user john
    Then the HTTP response status-code will be 403


  Scenario: Account to unfollow belongs to a moderator
  The account to unfollow must be from an user

    Given the user "max" is authenticated
    When a client sends a DELETE "/users/4/follower" request for user "max" to unfollow a moderator
    Then the HTTP response status-code will be 400
    And the HTTP response body contains following JSON of an error message:
      """
      {
        "errorMessage": "Specified account to unfollow belongs to a moderator"
      }
      """


  Scenario: Specified user to unfollow doesn´t exist
  The user to unfollow must exist

    Given the user "max" is authenticated
    But there is no user with id 9999
    When a client sends a DELETE "/users/9999/follower" request for user "max" to unfollow the specified user
    Then the HTTP response status-code will be 404