Feature: Get detailed information about a specified tweet
  This feature file describes the behaviour of the REST-API for GET requests at the endpoint /api/tweets/{tweetId} to get detailed information about a specified tweet.
  There should be follwoing behaviour at the REST-API:
  - If the request contains the header "Authorization" with a valid token of an account, then the http response will contain detailed information about the specified tweet and the http status-code will be 200
  - If the request doesn´t contain a valid token, then the http response status-code will be 401
  - If the specified tweet doesn´t exist or is in state "CANCELED", then the http response status-code will be 404

  Scenario: Request for user to get detailed information about a specified tweet
  Requesting information about a specified tweet

    Given the user "john" is authenticated
    And a stored tweet with id 1 from user max with content "Example content"
    And the tweet with id 1 has one liker and two retweets
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
            "numRetweets": 2,
            "rootTweet": null
        }
        """


    #fürBA: Immer noch nicht ausreichend.. Immer noch offen, was mit den Likern des Root-Tweets passiert.. werden die mit übernommen? Hier hat Entwickler Spielraum und kann es so implementieren, wie er meint
  Scenario: Tweet to get detailed information about is a retweet
  Requesting information about a specified retweet

    Given the user "max" is authenticated
    And a stored tweet with id 1 from user john and content "Example content" has a retweet with id 2 from user jane
    And the retweet hasn´t got liked
    When a client sends a GET "/tweets/2" request for user "max" to get detailed information about the retweet with id 2
    Then the HTTP response status-code will be 200
    And the HTTP response body will contain following JSON with detailed information about the retweet with id 2
        """
        {
            "tweetId": 2,
            "content": "Example content",
            "pubDate": 679658765,
            "author": {
                "userId": 0,
                "firstName": "Max",
                "lastName": "Mustermann"
            },
            "numLiker": 0,
            "numRetweets": 0,
            "rootTweet": {
                "tweetId": 1,
                "content": "Example content",
                "pubDate": 679658765,
                "author": {
                    "userId": 2,
                    "firstName": "John",
                    "lastName": "Doe"
                },
                "rootTweet": null
            }
        }
        """

  Scenario: Request for moderator to get detailed information about a specified tweet
  Requesting information about a specified tweet

    Given the moderator "werner" is authenticated
    And a stored tweet with id 1 from user max with content "Example content"
    And the tweet with id 1 has one liker and two retweets
    When a client sends a GET "/tweets/1" request for moderator "werner" to get detailed information about the tweet with id 1
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