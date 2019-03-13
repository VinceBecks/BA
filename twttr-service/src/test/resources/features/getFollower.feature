Feature: Show follower
  This feature file describes the behaviour of the system for GET requests at the endpoint on /api/users/{userId}/follower for getting a list of follower of the specified user.
  There should be follwoing behaviour at the system:
  - If the request contains the header "Authorization" with a valid token of an user, then the http response will contain a list of users who follows the specified user and the http state will be 200
  - If the specified account belongs to a moderator, then the http response body contains an appropriate information and the http response state is 400
  - If the request doesn´t contain a valid token, then the http response body will be empty and its state will be 401
  - If the request contains a valid token which belongs to a moderator, then the http response body will be empty and its state will be 403
  - If the specified user doesn´t exist, then the http response body will be empty and its state will be 404

  Scenario: Get follower
  Requesting a list of follower of a specified user

    Given the user "max" is authenticated
    And the user john with id 2 has two followers jane and lena
    When a client sends a "GET" "/users/2/follower" request for user "max" to get a list of follower from user john
    Then the HTTP response status-code will be 200
    And the HTTP response body contains following JSON of the follower from user john
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

