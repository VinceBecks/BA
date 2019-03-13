Feature: Unlike a Tweet
  This feature file describes the behaviour of the system for DELETE requests at the endpoint on /api/tweets/{tweetId}/liker to unlike a specified tweet.
  There should be follwoing behaviour at the system:
  - If the request contains the header "Authorization" with a valid token of an user, then the requesting user isn´t a liker of the specified tweet and http response state will be 204
  - If the request doesn´t contain a valid token, then, if the requesting user was a liker of the specified tweet, he still is a liker of the specified tweet and the http response state will be 401
  - If the request contains a valid token which belongs to a moderator, then the http response state will be 403
  - If the specified tweet doesn´t exist or is in state "CANCELED", then the http response state will be 404


  Scenario Outline: Unlike a tweet
  Request to unlike a tweet

    Given the user "max" is authenticated
    And a stored tweet with id 1
    And the user max <is or is not> a liker of tweet with id 1
    When a client sends a "DELETE" "/tweets/1/liker" request for user "max" to unlike the tweet with id 1
    Then the HTTP response status-code will be <status code>

    Examples:
      | is or is not | status code |
      | is           | 204         |
      | is not       | 400         |


  Scenario: Unauthorised request to unlike a tweet
  The request must contain a valid token of a user

    When a client sends a request without a valid token to unlike a specified tweet
    Then the HTTP response status-code will be 401



  Scenario: Token from request to unlike a tweet belongs to a moderator
  Account must be from an user

    Given the moderator "werner" is authenticated
    When a client sends a request for moderator "werner" to unlike a specified tweet
    Then the HTTP response status-code will be 403



  Scenario: Specified tweet to unlike doesn´t exist
  The user must exist

    Given there is no tweet with id 9999
    When a client sends a request to unlike the tweet with id 9999
    Then the HTTP response status-code will be 404


  Scenario: Specified tweet to unlike is in status CANCELED
  The tweet must not be in status CANCELED

    Given a stored tweet with id 1 in status CANCELED from user "max"
    When a client sends a request to unlike the tweet with id 1
    Then the HTTP response status-code will be 404