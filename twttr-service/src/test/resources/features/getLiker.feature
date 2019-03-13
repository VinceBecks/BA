Feature: Get liker of a specified tweet
  This feature file describes the behaviour of the system for GET requests at the endpoint on /api/tweets/{tweetId}/liker for getting a list of liker of the specified tweet.
  There should be follwoing behaviour at the system:
  - If the request contains the header "Authorization" with a valid token of an user, then the http response will contain a list of users who liked the specified tweet and the http state will be 200
  - If the request doesn´t contain a valid token, then the http response body will be empty and its state will be 401
  - If the request contains a valid token which belongs to a moderator, then the http response body will be empty and its state will be 403
  - If the specified tweet doesn´t exist or is in state "CANCELED", then the http response body will be empty and its state will be 404


  Scenario: Get a list of liker of a specific tweet
  Requesting a list of liker of a specified tweet

    Given the user "max" is authenticated
    And a stored tweet with id 1
    And the tweet with id 1 got liked by users max and john
    When a client sends a "GET" "/tweets/1/liker" request for user "max" to get a list of likers of the tweet with id 1
    Then the HTTP response status-code will be 200
    And the HTTP response body will contain following JSON with a list of users who liked the stored tweet:
        """
          [
              {
                  "userId": 0,
                  "firstName": "Max",
                  "lastName": "Mustermann",
                  "role": "USER"
              },
              {
                  "userId": 2,
                  "firstName": "John",
                  "lastName": "Doe",
                  "role": "USER"
              }
          ]
        """


  Scenario: Request is not authorized
  The request must contain a valid token of a user

    When a client sends a request without a valid token to get a list of liker of a tweet
    Then the HTTP response status-code will be 401



  Scenario: Token belongs to a moderator
  Account must be from an user

    Given the moderator "werner" is authenticated
    When a client sends a request for moderator "werner" to get a list of liker of a tweet
    Then the HTTP response status-code will be 403



  Scenario: User to get the list of follower from doesn´t exist
  The user must exist

    Given there is no tweet with id 9999
    When a client sends a request to get a list of liker of the tweet with id 9999
    Then the HTTP response status-code will be 404


  Scenario: Tweet to get the list of retweeter from is in status CANCELED
  The tweet must not be in status CANCELED

    Given a stored tweet with id 1 in status CANCELED from user "max"
    When a client sends a request to get a list of liker from  the tweet with id 1
    Then the HTTP response status-code will be 404