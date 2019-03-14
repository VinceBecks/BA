Feature: Show follower
  This feature file describes the behaviour of the system for GET requests at the endpoint on /api/users/{userId}/follower for getting a list of follower of the specified user.
  There should be follwoing behaviour at the system:
  - If the request contains the header "Authorization" with a valid token of an user, then the http response will contain a list of users who follows the specified user and the http status-code will be 200
  - If the specified account to get a list of follower from belongs to a moderator, then the http response body contains an appropriate information and the http response status-code is 400
  - If the request doesn´t contain a valid token of an user, then the http response status-code will be 401
  - If the request contains a valid token which belongs to a moderator, then the http status-code will be 403
  - If the specified user doesn´t exist, then the http response status-code will be 404

  Scenario: Get follower of an specified user
  Requesting a list of follower of a specified user

    Given the user "max" is authenticated
    And the user john with id 2 has two followers jane and lena
    When a client sends a GET "/users/2/follower" request for user "max" to get a list of follower from user john
    Then the HTTP response status-code will be 200
    And the HTTP response body will contain following JSON with a list of users who follows the user john
        """
          [
              {
                  "userId": 3,
                  "firstName": "Jane",
                  "lastName": "Doe",
                  "role": "USER"
              },
              {
                  "userId": 6,
                  "firstName": "Lena",
                  "lastName": "Löchte",
                  "role": "USER"
              }
          ]
        """



  Scenario: Unauthorised request to get a list of follower from a specified user
  The request must contain a valid token of an user

    When a client sends a request without a valid token of an user to get a list of follower from a specified user
    Then the HTTP response status-code will be 401



  Scenario: Account to get the list of follower from belongs to a moderator
  The account to get the list of follower from must be from an user

    When a client sends a request to to get a list of follower from a moderator
    Then the HTTP response status-code will be 404



  Scenario: Transmitted token from the request to get a list of follower from a specified user belongs to a moderator
  Just users can request a list of follower from a specified user

    Given the moderator "werner" is authenticated
    When a client sends a request for moderator "werner" to get a list of follower from a user
    Then the HTTP response status-code will be 403



  Scenario: User to get the list of follower from doesn´t exist
  The user to get the list of follower from must be existing

    Given there is no user with id 9999
    When a client sends a request to get a list of follower from the user with id 9999
    Then the HTTP response status-code will be 404