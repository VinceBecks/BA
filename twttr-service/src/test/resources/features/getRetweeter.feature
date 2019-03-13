Feature: Get list of retweeter
  This feature file describes the behaviour of the system for GET requests at the endpoint on /api/tweets/{tweetId}/retweets/authors for getting a list of retweeter of the specified tweet.
  There should be follwoing behaviour at the system:
  - If the request contains the header "Authorization" with a valid token of an user, then the http response will contain a list of users who retweeted the specified tweet and the http state will be 200
  - If the request doesn´t contain a valid token, then the http response body will be empty and its state will be 401
  - If the request contains a valid token which belongs to a moderator, then the http response body will be empty and its state will be 403
  - If the specified tweet doesn´t exist or is in state "CANCELED", then the http response body will be empty and its state will be 404



  Scenario: Get a list of liker of a specific tweet
  Requesting a list of liker of a specified tweet

    Given an authenticated user with credentials
      | accountId | userName | password |
      | 1         | phillipp | password |
    And a stored tweet
    And the stored tweet got retweeted by following users with credentials:
      | accountId | userName  | password |
      | 2         | Simon     | password |
      | 3         | Lukas     | password |
    When a client sends a "GET" "/tweets/{tweetId}/retweets/authors" request to get a list of retweeter of the stored tweet with a valid token of the user with id 1
    Then the HTTP response state will be 200
    And the HTTP response body will contain following JSON with a list of users who retweeted the stored tweet:
        """
        [
            {
                "userId": 2,
                "firstName": "Simon",
                "lastName": "Lochny"
            },
            {
                "userId": 3,
                "firstName": "Lukas",
                "lastName": "Schröder"
            }
        ]
        """