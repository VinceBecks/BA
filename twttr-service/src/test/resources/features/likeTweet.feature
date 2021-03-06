Feature: Like a specified tweet
  This feature file describes the behaviour of the REST-API for POST requests at the endpoint /api/tweets/{tweetId}/liker to like a specified tweet.
  There should be follwoing behaviour at the REST-API:
  - If the request contains the header "Authorization" with a valid token of an user, then the http response status-code will be 204
  - If an user is already a liker of the specified tweet, then the http body will contain an appropriate information about the mistake and the status-code will be 400
  - If the request doesn´t contain a valid token, then the http response status-code will be 401
  - If the request contains a valid token which belongs to a moderator, then http response status-code will be 403
  - If the specified tweet doesn´t exist or is in status "CANCELED", then the http response status-code will be 404

  Scenario: Like a tweet
  Request to like a specified tweet

    Given the user "max" is authenticated
    And the user max is not a liker of a tweet with id 1
    When a client sends a POST "/tweets/1/liker" request for user "max" to like the specified tweet
    Then the client will receive the "NO_CONTENT" Status Code


  Scenario: Requesting user is already a liker of the specified tweet
  Each user can be just once a liker of a specified tweet

    Given the user "max" is authenticated
    And a stored tweet with id 1
    And the user max is a liker of tweet with id 1
    When a client sends a POST "/tweets/1/liker" request for user "max" to like the specified tweet
    Then the client will receive the "BAD_REQUEST" Status Code
    And the HTTP response body contains following JSON of an error message:
      """
      {
        "errorMessage": "Requesting user is already a liker of the specified tweet"
      }
      """


  Scenario: Unauthorised request to like a specified tweet
  The request must contain a valid token of an user

    Given a stored tweet with id 1
    When a client sends a POST "/tweets/1/liker" request without a valid token to like the specified tweet
    Then the client will receive the "UNAUTHORIZED" Status Code


  Scenario: Transmitted token from the request to like a specified tweet belongs to a moderator
  Just users can like tweets

    Given the moderator "werner" is authenticated
    And a stored tweet with id 1
    When a client sends a POST "/tweets/1/liker" request for moderator "werner" to like the specified tweet
    Then the client will receive the "FORBIDDEN" Status Code


  Scenario: The tweet to like doesn´t exist
  The tweet to like must be existing

    Given the user "max" is authenticated
    And there is no tweet with id 9999
    When a client sends a POST "/tweets/9999/liker" request for user "max" to like the specified tweet
    Then the client will receive the "NOT_FOUND" Status Code


  Scenario: Tweet to like is in status CANCELED
  The tweet to like must be in status PUBLISH

    Given the user "max" is authenticated
    And a stored tweet with id 1 in status CANCELED from user max
    When a client sends a POST "/tweets/1/liker" request for user "max" to like the specified tweet
    Then the client will receive the "NOT_FOUND" Status Code


