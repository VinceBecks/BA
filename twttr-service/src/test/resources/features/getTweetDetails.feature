Feature: Get detailed information about a specified tweet
  This feature file describes the behaviour of the REST-API for GET requests at the endpoint /api/tweets/{tweetId} to get detailed information about a specified tweet.
  There should be follwoing behaviour at the REST-API:
  - If the request contains the header "Authorization" with a valid token of an account, then the http response will contain detailed information about the specified tweet and the http status-code will be 200
  - If the request doesn´t contain a valid token, then the http response status-code will be 401
  - If the specified tweet doesn´t exist or is in state "CANCELED", then the http response status-code will be 404


  #todo: Szenario für moderator schreiben
  Scenario: Get information about specified tweet
  Requesting information about a specified tweet

    Given the user "john" is authenticated
    And a stored tweet with id 1 from user max with content "Example Content"
    And the tweet with id 1 got liked by 1 user and retweeted by 2 users
    When a client sends a GET "/tweets/1" request for user "john" to get detailed information about the tweet with id 1
    Then the HTTP response status-code will be 200
    And the HTTP response body will contain following JSON with detailed information about the tweet with id 1
        """
        {
            "tweetId": 1,
            "content": "Example content",
            "pubDate": 679658765,
            "author": {
                "userId": 0,
                "firstName": "Max",
                "lastName": "Mustermann"
            },
            "numLiker": 1,
            "numRetweets": 2
        }
        """



  Scenario: Unauthorised request to get a detailed information about a specified tweet
  The request must contain a valid token

    When a client sends a request without a valid token to get detailed information about a tweet
    Then the HTTP response status-code will be 401



  Scenario: Tweet to get detailed information about doesn´t exist
  The tweet to get detailed information about must exist

    Given there is no tweet with id 9999
    When a client sends a request to get detailed information about the tweet with id 9999
    Then the HTTP response status-code will be 404



  Scenario: Tweet to get detailed information about is in status CANCELED
  The tweet to get detailed information about must be in status PUBLISH

    Given a stored tweet with id 1 in status CANCELED from user max
    When a client sends a request to get detailed information about the tweet with id 1
    Then the HTTP response status-code will be 404