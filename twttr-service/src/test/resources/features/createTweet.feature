Feature: Create tweet
  This feature file describes the behaviour of the system for POST requests at the endpoint on /api/tweets for creating new tweets.
  There should be follwoing behaviour at the system:
  - If a tweet with a content length between 1 and 140 is transmitted and the request contains the header "Authorization" with a valid token of an user, then a new tweet will be created for the requesting user and the http response state will be 201
  - If the transmitted content for the new tweet has a size which is lower than 1 or higher than 140, then there will be no tweet created and the http response will contain an appropriate information and the state will be 400
  - If the request doesn´t contain a valid token, then there will be no tweet created and the http response state will be 401
  - If the transmitted token belongs to a moderator, then there will be no tweet created and the http response state will be 403



  Scenario: Create a new tweet
  To create a new tweet a client must send a POST /api/tweets request with a valid token from an user and a JSON with the attribute "content"

    Given the user "max" is authenticated
    When a client sends a "POST" "/tweets" request for the user "max" to create a new tweet and the body contains following JSON structure
      | attribute | type   | value              |
      | content   | String | An example content |
    Then the HTTP response state will be 201
    And the HTTP response body contains following JSON of a new Tweet, while the tweetId and the publish-date got generated by the system
        """
            {
                "tweetId": 5,
                "content": "An example content",
                "author" : {
                    "userId": 1,
                    "firstName": "Phillipp",
                    "lastName": "Otto",
                    "role": "USER"
                },
                "rootTweet": null
            }
        """