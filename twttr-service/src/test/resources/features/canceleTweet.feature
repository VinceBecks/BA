Feature: Delete a tweet
  This feature file describes the behaviour of the system for DELETE requests at the endpoint on /api/tweets/{tweetId} for deleting a specified tweets.
  There should be follwoing behaviour at the system:
  - If the request contains the header "Authorization" with a valid token of the user who is the author of the specified tweet, then the state of the specified tweet will be settet to "CANCELED" and the http response state will be 204
  - If the request contains a valid token which belongs to a moderator, then the state of the specified tweet will be settet to "CANCELED" and the http response state will be 204
  - If the request contains a token from another user than the author, then the state of the specified tweet will not be changed and the http response state will be 403
  - If the request doesn´t contain a valid token, then the state of the specified tweet will not be changed and the http response state will be 401
  - If the specified tweet doesn´t exist or is already in state "CANCELED", then the state of the specified tweet will not be changed and the http response state will be 404


  Scenario: Like a tweet
  Request to like a specified tweet

    Given the user "max" is authenticated
    And a stored tweet from "max" with id 1
    When a client sends a "DELETE" "/tweets/1" request for user "max"
    Then the HTTP response state will be 204