Feature: Show details of a Tweet
  This feature file describes the behaviour of the system for GET requests at the endpoint on /api/tweets/{tweetId} for getting more information about a specified tweet.
  There should be follwoing behaviour at the system:
  - If the request contains the header "Authorization" with a valid token of an account, then the http response will contain the important information about the specified tweet and the http state will be 200
  - If the request doesn´t contain a valid token, then the http response body will be empty and its state will be 401
  - If the specified tweet doesn´t exist or is in state "CANCELED", then the http response body will be empty and its state will be 404




  Scenario: Get information about specified tweet
  Requesting information about a specified tweet

    Given the user "john" is authenticated
    And a stored tweet with id 1 from user max with content "Example Content"
    And the tweet with id 1 got liked by 1 user and retweeted by 2 users
    When a client sends a "GET" "/tweets/1" request for user "john" to get detailed information about the tweet with id 1
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