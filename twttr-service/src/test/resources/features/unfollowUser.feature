Feature: Unfollow a user
  This feature file describes the behaviour of the system for DELETE requests at the endpoint on /api/users/{userId}/follower to unfollow a specified user.
  There should be follwoing behaviour at the system:
  - If the request contains the header "Authorization" with a valid token of an user, then the requesting user isn´t a follower of the specified user and http response state will be 204
  - If the specified account belongs to a moderator, then the http response body contains an appropriate information and the http response state will be 404
  - If the request doesn´t contain a valid token, then, if the requesting user was a follower of the specified user, he still is a follower of the specified user and the http response state will be 401
  - If the request contains a valid token which belongs to a moderator, then the http response state will be 403
  - If the specified user doesn´t exist, then the http response state will be 404


  Scenario Outline: Like a tweet
  Request to like a specified tweet

    Given the user "max" is authenticated
    And the user max <is or is not> a follower of user john with id 2
    When a client sends a "DELETE" "/users/2/follower" request for user "max" to unfollow user john
    Then the HTTP response status-code will be <status code>
    #todo response objekt error


    Examples:
      | is or is not | status code |
      | is           | 400         |
      | is not       | 204         |



  Scenario: Unauthorised request to unfollow a user
  The request must contain a valid token of a user

    When a client sends a request without a valid token to unfollow a specified user
    Then the HTTP response status-code will be 401



  Scenario: Token from request to unfollow a user belongs to a moderator
  Account must be from an user

    Given the moderator "werner" is authenticated
    When a client sends a request for moderator "werner" to unfollow a specified user
    Then the HTTP response status-code will be 403



  Scenario: Specified user to unfollow doesn´t exist
  The user must exist

    Given there is no user with id 9999
    When a client sends a request to unfollow the user with id 9999
    Then the HTTP response status-code will be 404