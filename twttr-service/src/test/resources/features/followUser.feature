Feature: Follow a Member
  This feature file describes the behaviour of the system for POST requests at the endpoint on /api/users/{userId}/follower to follow a specified user.
  There should be follwoing behaviour at the system:
  - If the request contains the header "Authorization" with a valid token of an user, then the requesting user is a follower of the specified user and the http response state will be 204
  - If a user is already a follower of the specified user, then he will not be a second time a follower of the specified user
  - If the specified account belongs to a moderator, then the requesting user will not follow the moderator and the http response body will contain an approptiate answer and the http response state will be 400
  - If the request doesn´t contain a valid token, then the requesting user isn´t a new follower of the specified user and the http response state will be 401
  - If the request contains a valid token which belongs to a moderator, then the specified user will not have a new follower and the http response state will be 403
  - If the specified user doesn´t exist, then the http response body will be empty and its state will be 404

  Scenario: Follow a user
  Request to follow a user

    #todo: bei dem Step mit dem request immer schreiben "with a valid token"?
    Given the user "max" is authenticated
    And the user "max" isn´t a follower of user "john" with id 2
    When a client sends a "POST" "/users/2/follower" request for user "max" to follow user "john"
    Then the user "max" will be a follower of user "john"
    And the HTTP response state will be 204