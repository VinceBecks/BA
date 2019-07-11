Feature: Create tweet
  This feature file describes the behaviour of the REST-API for POST requests at the endpoint /api/tweets to create a new tweet.
  There should be follwoing behaviour at the REST-API:
  - Users can create new tweets with a size between 1 and 140
  - The size of tweets has to be between 1 and 140 character
  - The request has to contain an valid token to craete a new tweet
  - Just Users are allowed to share tweets

  #fürBA: 2 unterschiedliche Weisen wie JSON Datei beschrieben werden kann

  Scenario: Create a new tweet
  Request to create a new tweet and the tweetId will be generated from the database.
  The date of publish will also from the system automatically assigned

    Given the user "max" is authenticated
    When a client sends a POST "/tweets" request for user "max" to create the following tweet
      | attribute | type   | value              |
      | content   | String | An example content |
    Then the client will receive the "CREATED" Status Code
    And the HTTP response body contains following JSON of a new Tweet, while the tweetId and the publish-date got generated by the system
        """
            {
                "tweetId": 5,
                "content": "An example content",
                "pubDate": "04.07.2019 13:49",
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
    When a client sends a POST "/tweets" request for user "max" to create new tweet with <numberOfCharacter> character
    Then the client will receive the "BAD_REQUEST" Status Code
    And the HTTP response body contains following JSON of a list of error messages:
      """
      [
        {
          "errorMessage": "Wrong number of character"
        }
      ]
      """

    Examples:
      | numberOfCharacter |
      | 0                  |
      | 141                |



  Scenario: Unauthorised request to create a new tweet
  The request must contain a valid token of an user to create a new tweet

    When a client sends a POST "/tweets" request for user "max" without a valid token to create the following tweet
      | attribute | type   | value              |
      | content   | String | An example content |
    Then the client will receive the "UNAUTHORIZED" Status Code



  Scenario: Transmitted token from the request to create a new tweet belongs to a moderator
  Just users can create new tweets

    Given the moderator "werner" is authenticated
    When a client sends a POST "/tweets" request for moderator "werner" to create the following tweet
      | attribute | type   | value              |
      | content   | String | An example content |
    Then the client will receive the "FORBIDDEN" Status Code
