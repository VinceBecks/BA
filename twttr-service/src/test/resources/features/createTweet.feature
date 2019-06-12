Feature: Create tweet
  This feature file describes the behaviour of the REST-API for POST requests at the endpoint /api/tweets to create a new tweet.
  There should be follwoing behaviour at the REST-API:
  - If a tweet with a content length between 1 and 140 is transmitted and the request contains the header "Authorization" with a valid token of an user, then the http response body will contain a JSON of the new created tweet and the status-code will be 201
  - If the transmitted content for the new tweet has a size which is lower than 1 or higher than 140, then the http response will contain an appropriate information about the mistake and the status-code will be 400
  - If the request doesn´t contain a valid token of an user, then the http response status-code will be 401
  - If the transmitted token belongs to a moderator, then the http response status-code will be 403


  Scenario: Create a new tweet
  Request to create a new tweet and the tweetId will be genarated from the database.
  The date of publish will also from the system automatically assigned

    Given the user "max" is authenticated
    When a client sends a POST "/tweets" request for the user "max" to create a new tweet and the body contains following JSON
      | attribute | type   | value              |
      | content   | String | An example content |
    Then the HTTP response status-code will be 201
    And the HTTP response body contains following JSON of a new Tweet, while the tweetId and the publish-date got generated by the system
        """
            {
                "tweetId": 5,
                "content": "An example content",
                "author" : {
                    "userId": 0,
                    "firstName": "Max",
                    "lastName": "Mustermann",
                    "role": "USER"
                },
                "rootTweet": null
            }
        """

  Scenario Outline: Wrong number of characters for the new tweet to be created
  The number of characters for the new tweet should be between 1 and 140

    Given the user "max" is authenticated
    When a client sends a request for "max" to create a new tweet with <numberOfCharacters> characters
    Then the HTTP response status-code will be 400
    And the HTTP response body contains following JSON of a list of error messages:
      """
      [
        {
          "errorMessage": "Wrong number of character"
        }
      ]
      """

    Examples:
      | numberOfCharacters |
      | 0                  |
      | 141                |



  Scenario: Unauthorised request to create a new tweet
  The request must contain a valid token of an user to create a new tweet

    When a client sends a request to create a new tweet without a valid token of an user
    Then the HTTP response status-code will be 401



  Scenario: Transmitted token from the request to create a new tweet belongs to a moderator
  Just users can create new tweets

    Given the moderator "werner" is authenticated
    When a client sends a request to create a tweet for the moderator "werner"
    Then the HTTP response status-code will be 403
