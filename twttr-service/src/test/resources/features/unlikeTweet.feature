Feature: Unlike a specified tweet
  This feature file describes the behaviour of the REST-API for DELETE requests at the endpoint /api/tweets/{tweetId}/liker to unlike a specified tweet.
  There should be follwoing behaviour at the REST-API:
  - If the request contains the header "Authorization" with a valid token of an user, then the http response status-code will be 204
  - If the request doesn´t contain a valid token, then the http response status-code will be 401
  - If the request contains a valid token which belongs to a moderator, then the http response status-code will be 403
  - If the specified tweet doesn´t exist or is in state "CANCELED", then the http response status-code will be 404


  Scenario Outline: Unlike a tweet
  Request to unlike a tweet

    Given the user "max" is authenticated
    And a stored tweet with id 1
    And the user max <is or is not> a liker of tweet with id 1
    When a client sends a DELETE "/tweets/1/liker" request for user "max" to unlike the tweet with id 1
    Then the HTTP response status-code will be <status code>

    Examples:
      | is or is not | status code |
      | is           | 204         |
      | is not       | 400         |


  Scenario: Unauthorised request to unlike a tweet
  The request must contain a valid token of an user

    When a client sends a request without a valid token of an user to unlike a specified tweet
    Then the HTTP response status-code will be 401



  Scenario: Token from request to unlike a tweet belongs to a moderator
  Account must be from an user

    Given the moderator "werner" is authenticated
    When a client sends a request for moderator "werner" to unlike a specified tweet
    Then the HTTP response status-code will be 403


  #fürBA: Hier geht es nicht, dass gleiche Methode auf verschiedene Steps (unfollow, unlike, ... --> alle sehr ähnlich, aber unterschiedliche Pfade, bzw. methoden) matched, da es unterschiedliche Pfade sind
  Scenario: Tweet to unlike doesn´t exist
  The tweet to unlike must exist

    Given there is no tweet with id 9999
    When a client sends a request to unlike the tweet with id 9999
    Then the HTTP response status-code will be 404


  Scenario: Tweet to unlike is in status CANCELED
  The tweet to unlike must be in status PUBLISH

    Given a stored tweet with id 1 in status CANCELED from user max
    When a client sends a request to unlike the tweet with id 1
    Then the HTTP response status-code will be 404