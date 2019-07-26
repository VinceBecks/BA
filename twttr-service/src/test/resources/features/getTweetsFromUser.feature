Feature: Get tweets from user
  This feature file describes the behaviour of the system for GET requests at the endpoint on /api/{userId}/tweets for receiving a sorted list of the last tweets the specified tweet.
  - If the request contains the header "Authorization" with a valid token, then the http response body contains a list of the last 3 tweets in state "PUBLISH" from the specified user, sorted by theIR publish date and the http response status-code will be 200
  - If the QueryParam "index" of the request is higher than 0, then its value describes how many of the last tweets in status "PUBLISH" will be skipped for the response
  - If the QueryParam "numTweets" is higher than 0, then the response contains as many tweets in status "PUBLISH" as its value, if there are enough
  - If the QueryParam "index" isn´t setted, then its default value will be 0
  - If the QueryParam "numTweets" isn´t setted, then its default value will be 3
  - If the number of requested tweets is higher than the list of tweets, or the difference of the list of tweets and the index to start from, then all remaining tweets will be responded and the http response status-code will be 200
  - If the QueryParam "index" or the QueryParam "numTweets" isn´t a positive integer, then there will be no tweets responded and the http response will contain an appropriate information about the mistake and the status-code will be 400
  - If the request doesn´t contain a valid token, then the http response status-code will be 401


  Background: Authenticate user max and persist tweets from john
    Given the user "max" is authenticated
    And the moderator "werner" is authenticated
    And following tweets got persisted from user john with id 2 in presented order
      | tweetId | content   | state    |
      | 1       | 1. tweet  | PUBLISH  |
      | 2       | 2. tweet  | PUBLISH  |
      | 3       | 3. tweet  | CANCELED |
      | 4       | 4. tweet  | PUBLISH  |
      | 5       | 5. tweet  | PUBLISH  |
      | 6       | 6. tweet  | CANCELED |
      | 7       | 7. tweet  | PUBLISH  |
      | 8       | 8. tweet  | PUBLISH  |
      | 9       | 9. tweet  | PUBLISH  |
      | 10      | 10. tweet | PUBLISH  |





  Scenario: Get tweets of a user
    When a client sends a GET "/users/2/tweets" request for user "max" to get a list of tweets from user john
    Then the client will receive the "OK" Status Code
    And the HTTP response body will contain following JSON with tweets from user john
    """
    [
    {
        "tweetId": 10,
        "content": "10. tweet",
        "pubDate": "04.07.2019 13:49",
        "author": {
            "userId": 2,
            "firstName": "John",
            "lastName": "Doe",
            "role": "USER"
        },
        "rootTweet": null
    },
    {
        "tweetId": 9,
        "content": "9. tweet",
        "pubDate": "04.07.2019 13:48",
        "author": {
            "userId": 2,
            "firstName": "John",
            "lastName": "Doe",
            "role": "USER"
        },
        "rootTweet": null
    },
    {
        "tweetId": 8,
        "content": "8. tweet",
        "pubDate": "04.07.2019 13:47",
        "author": {
            "userId": 2,
            "firstName": "John",
            "lastName": "Doe",
            "role": "USER"
        },
        "rootTweet": null
    }
    ]
    """




  Scenario: Moderator requests tweets from an user
    Given the moderator "werner" is authenticated
    When a client sends a GET "/users/2/tweets" request for moderator "werner" to get a list of tweets from user john
    Then the client will receive the "OK" Status Code
    And the HTTP response body will contain following JSON with tweets from user john
    """
    [
    {
        "tweetId": 10,
        "content": "10. tweet",
        "pubDate": "04.07.2019 13:49",
        "author": {
            "userId": 2,
            "firstName": "John",
            "lastName": "Doe",
            "role": "USER"
        },
        "rootTweet": null
    },
    {
        "tweetId": 9,
        "content": "9. tweet",
        "pubDate": "04.07.2019 13:48",
        "author": {
            "userId": 2,
            "firstName": "John",
            "lastName": "Doe",
            "role": "USER"
        },
        "rootTweet": null
    },
    {
        "tweetId": 8,
        "content": "8. tweet",
        "pubDate": "04.07.2019 13:47",
        "author": {
            "userId": 2,
            "firstName": "John",
            "lastName": "Doe",
            "role": "USER"
        },
        "rootTweet": null
    }
    ]
    """






  Scenario Outline: Change QueryParams
  The QueryParam numTweets represents the number of requested tweets
  The default value for numTweets will be 3
  The QueryParam index represents the number of tweets to be skipped for the response from a list of all PUBLISH tweets from the specified user sorted by their publish date
  The default value for index will be 0

    When a client sends a GET "/users/2/tweets" request for user "max" to get a list of tweets from user john with following Query Params
      | queryParam: | numTweets   | index   |
      | value:      | <numTweets> | <index> |
    Then the client will receive the "OK" Status Code
    And the HTTP response body contains the tweets with the ids <testIds> in presented order

    Examples: No params are setted
      | numTweets  | index      | testIds          |
      | not setted | not setted | 10,9,8           |

    Examples: Just index param is setted
      | numTweets  | index      | testIds          |
      | not setted | 2          | 8,7,5            |
      | not setted | 0          | 10,9,8           |
      | not setted | 7          | 1                |
      | not setted | 8          |                  |

    Examples: Just numTweets param is setted
      | numTweets  | index      | testIds          |
      | 3          | not setted | 10,9,8           |
      | 6          | not setted | 10,9,8,7,5,4     |
      | 8          | not setted | 10,9,8,7,5,4,2,1 |
      | 9          | not setted | 10,9,8,7,5,4,2,1 |

    Examples: Both params are setted
      | numTweets  | index      | testIds          |
      | 9          | 1          | 9,8,7,5,4,2,1    |




  Scenario: Unauthorised request to get tweets from a user
  The request must contain a valid token of a user

    When a client sends a GET "/users/2/tweets" request without a valid token to get a list of tweets from user john
    Then the client will receive the "UNAUTHORIZED" Status Code