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
    And a stored tweet with id 1 from user "max"
    When a client sends a "DELETE" "/tweets/1" request for user "max"
    Then the HTTP response status-code will be 204


  Scenario: Requesting user isn´t the author
  Users can just delete their own tweets

    #todo: im "And" Part schreiben, dass der Tweet in Status PUBLISH ist?
    Given the user "max" is authenticated
    And a stored tweet with id 1 from user "john"
    When a client sends a request for user "max" to delete the tweet with id 1
    Then the HTTP response status-code will be 403


  Scenario: Moderator deletes tweet
  Moderators can delete the tweets from every user

    Given the moderator "werner" is authenticated
    And a stored tweet with id 1 from user "max"
    When a client sends a request for moderator "werner" to delete the tweet with id 1
    Then the HTTP response status-code will be 204



    #todo: prüfen, ob es tweet nach request noch gibt? und in status PUBLISH ist?
  Scenario: Request is not authorized
  The request must contain a valid token of a user

    Given a stored tweet with id 1
    When a client sends a request without a valid token to delete the tweet with id 1
    Then the HTTP response status-code will be 401


  Scenario: Tweet doesn´t exist
  The tweet must exist

    Given the user "max" is authenticated
    But there is no tweet with id 9999
    When a client sends a request for user "max" to delete the tweet with id 9999
    Then the HTTP response status-code will be 404



  Scenario: Tweet to delete is in status CANCELED
  The tweet must exist

    Given the user "max" is authenticated
    And a stored tweet with id 1 in status CANCELED from user "max"
    When a client sends a request for user "max" to delete the tweet with id 1
    Then the HTTP response status-code will be 404