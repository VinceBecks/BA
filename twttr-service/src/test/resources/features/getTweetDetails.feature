Feature: Show details of a Tweet
  This feature file describes the behaviour of the system for GET requests at the endpoint on /api/tweets/{tweetId} for getting more information about a specified tweet.
  There should be follwoing behaviour at the system:
  - If the request contains the header "Authorization" with a valid token of an account, then the http response will contain the important information about the specified tweet and the http state will be 200
  - If the request doesn´t contain a valid token, then the http response body will be empty and its state will be 401
  - If the specified tweet doesn´t exist or is in state "CANCELED", then the http response body will be empty and its state will be 404




  Scenario: Get information about specified tweet
  Requesting information about a specified tweet

    Given a stored tweet
    And an authenticated user with credentials
      | accountId | userName | password |
      | 1         | phillipp | password |
    When a client sends a "GET" "/tweets/{tweetId}" request with valid token from user with id 1 to get the details of the stored tweet
    Then the HTTP response state will be 200
    And the HTTP response body contains a JSON of the tweet details with following structure
        """
        {
            "tweetId": 1,
            "content": "More Content",
            "pubDate": 679658765,
            "author": {
                "userId": 3,
                "firstName": "Lukas",
                "lastName": "Schröder"
            },
            "numLiker": 1,
            "numRetweets": 2
        }
        """