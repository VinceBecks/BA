Feature: Follow a Member
  This feature file describes the behaviour of the system for POST requests at the endpoint on /api/users/{userId}/follower to follow a specified user.
  There should be follwoing behaviour at the system:
  - If the request contains the header "Authorization" with a valid token of an user, then the http response status-code will be 204
  - If the requesting user is already a follower of the specified user, then the http response status-code will be 400
  - If the request doesn´t contain a valid token of an user, then the http response status-code will be 401
  - If the specified account to follow belongs to a moderator, then the http response body will contain an appropriate information about the mistake and the http response status-code will be 400
  - If the request contains a valid token which belongs to a moderator, then the http response status-code will be 403
  - If the specified user to follow doesn´t exist, then the http response status-code will be 404

  Scenario: Follow a user
  Request to follow a user

    Given the user "max" is authenticated
    And the user max isn´t a follower of user john with id 2
    When a client sends a POST "/users/2/follower" request for user "max" to follow user john
    Then the HTTP response status-code will be 204


  Scenario: Requesting user is already a follower of the specified user
  Each user can be just once a follower of a specified user

    Given the user "max" is authenticated
    And the user max is already a follower of user john
    When a client sends a request for user "max" to follow user "john"
    Then the HTTP response status-code will be 400
    And the HTTP response body contains following JSON of an error message:
      """
      {
        "errorMessage": "User is already a follower of the specified user"
      }
      """


  Scenario: Unauthorised request to follow a user
  The request to follow an user must contain a valid token of an user

    When a client sends a request to follow an user without a valid token
    Then the HTTP response status-code will be 401



  Scenario: Account to follow belongs to a moderator
  The account to follow must be from an user

    When a client sends a request to follow the account of a moderator
    Then the HTTP response status-code will be 404



  Scenario: Transmitted token from the request to follow a specified user belongs to a moderator
  Just users can follow users

    Given the moderator "werner" is authenticated
    When a client sends a request for the moderator "werner" to follow a user
    Then the HTTP response status-code will be 403



  Scenario: The user to follow doesn´t exist
  The account from an user to follow must be existing

    Given there is no user with id 9999
    When a client sends a request to follow the user with id 9999
    Then the HTTP response status-code will be 404