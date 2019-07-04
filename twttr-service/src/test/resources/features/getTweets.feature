Feature: Get all Tweets
  This feature file describes the behaviour of the system for GET requests at the endpoint on /api/tweets for receiving a sorted list of the last tweets of users the requesint user is following
  There should be follwoing behaviour at the system:
  - If the request contains the header "Authorization" with a valid token of an user, then the http response body contains a list of the last 3 tweets in status "PUBLISH" from users the requesting user is following, sorted by their publish date and the http response status-code will be 200
  - If the request contains the header "Authorization" with a valid token of a moderator, then the http response body contains a list of the last 3 tweets in status "PUBLISH" from all users, sorted by their publish date and the http response status-code will be 200
  - If the QueryParam "index" of the request is higher than 0, then its value describes how many of the last tweets in state "PUBLISH" will be skipped for the response from the list of the last tweet in state PUBLISH
  - If the QueryParam "numTweets" is higher than 0, then the response contains as many tweets in state "PUBLISH" as its value, if there are enough matching PUBLISH tweets persisted
  - If the QueryParam "index" isn´t setted, then its default value will be 0
  - If the QueryParam "numTweets" isn´t setted, then its default value will be 3
  - If the number of requested tweets is higher than the list of tweets, or the difference of the list of tweets and the index to start from, then all remaining tweets will be responded and the http response status-code will be 200
  - If the QueryParam "index" or the QueryParam "numTweets" isn´t a positive integer, then the http response body will contain an appropriate information about the mistake and the status-code will be 400
  - If the request doesn´t contain a valid token, then the http response status-code will be 401

  Background: Authenticate user max and moderator werner, persist tweets and let user max follow users john and jane
    Given the user "max" is authenticated
    And the moderator "werner" is authenticated
    And following tweets got persisted in presented order
      | tweetId | content   | state    | author |
      | 1       | 1. tweet  | PUBLISH  | john   |
      | 2       | 2. tweet  | PUBLISH  | jane   |
      | 3       | 3. tweet  | CANCELED | john   |
      | 4       | 4. tweet  | PUBLISH  | max    |
      | 5       | 5. tweet  | PUBLISH  | john   |
      | 6       | 6. tweet  | CANCELED | karl   |
      | 7       | 7. tweet  | PUBLISH  | jane   |
      | 8       | 8. tweet  | PUBLISH  | lena   |
      | 9       | 9. tweet  | PUBLISH  | jane   |
      | 10      | 10. tweet | PUBLISH  | jane   |





  Scenario: User requests last tweets from users he is following
  The last three published tweets will be responded when the queryParams default-values will not be overwritten
  The response will contain only the tweets from users the requesting user is following

    And user max follows the users john and jane
    When a client sends a GET "/tweets" request for user "max" to get a list of tweets
    Then the HTTP response status-code will be 200
    And the HTTP response body will contain following JSON with tweets from users max is following
    """
    [
    {
        "tweetId": 10,
        "content": "10. tweet",
        "pubDate": "04.07.2019 13:49",
        "author": {
            "userId": 3,
            "firstName": "Jane",
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
            "userId": 3,
            "firstName": "Jane",
            "lastName": "Doe",
            "role": "USER"
        },
        "rootTweet": null
    },
    {
        "tweetId": 7,
        "content": "7. tweet",
        "pubDate": "04.07.2019 13:47",
        "author": {
            "userId": 3,
            "firstName": "Jane",
            "lastName": "Doe",
            "role": "USER"
        },
        "rootTweet": null
    }
    ]
    """


  Scenario Outline: User change QueryParams for request to get tweet information
  The QueryParam numTweets represents the number of requested tweets and will overwrite its default value 3
  The QueryParam index determines how many of the newest tweets in state PUBLISH should be skipped
  The client will still receive just tweets from users the requesting user is following

    And user max follows the users john and jane
    When a client sends a GET "/tweets" request for user "max" to get a list of tweets with following Query Params
      | queryParam: | numTweets   | index   |
      | value:      | <numTweets> | <index> |
    Then the HTTP response status-code will be 200
    And the HTTP response body contains the tweets with the ids <tweetIds> in presented order

    Examples: No params are setted
      | numTweets  | index      | tweetIds     |
      | not setted | not setted | 10,9,7       |

    Examples: Just index param is setted
      | numTweets  | index      | tweetIds     |
      | not setted | 2          | 7,5,2        |
      | not setted | 0          | 10,9,7       |
      | not setted | 5          | 1            |
      | not setted | 6          |              |

    Examples: Just numTweets param is setted
      | numTweets  | index      | tweetIds     |
      | 3          | not setted | 10,9,7       |
      | 6          | not setted | 10,9,7,5,2,1 |
      | 7          | not setted | 10,9,7,5,2,1 |

    Examples: Both params are setted
      | numTweets  | index      | tweetIds     |
      | 3          | 1          | 9,7,5        |





  Scenario: Moderator requests last tweets from all users
  The response for a request from a moderator will contain tweets from all users
  The last three published tweets will be responded when the queryParams default-values will not be overwritten

    And user max follows the users john and jane
    When a client sends a GET "/tweets" request for moderator "werner" to get a list of tweets
    Then the HTTP response status-code will be 200
    And the HTTP response body will contain following JSON with tweets from users max is following
    """
    [
    {
        "tweetId": 10,
        "content": "10. tweet",
        "pubDate": "04.07.2019 13:49",
        "author": {
            "userId": 3,
            "firstName": "Jane",
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
            "userId": 3,
            "firstName": "Jane",
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
            "userId": 6,
            "firstName": "Lena",
            "lastName": "Löchte",
            "role": "USER"
        },
        "rootTweet": null
    }
    ]
    """


  Scenario Outline: Moderator changes QueryParams for request to get tweet information
  The QueryParam numTweets represents the number of requested tweets
  The QueryParam index determines how many of the newest tweets in state PUBLISH should be skipped
  The response for a request from a moderator will still contain tweets from all users

    When a client sends a GET "/tweets" request for moderator "werner" to get a list of tweets with following Query Params
      | queryParam: | numTweets   | index   |
      | value:      | <numTweets> | <index> |
    Then the HTTP response status-code will be 200
    And the HTTP response body contains the tweets with the ids <testIds> in presented order

    Examples: No params are setted
      | numTweets  | index      | testIds |
      | not setted | not setted | 10,9,8  |

    Examples: Just index param is setted
      | numTweets  | index      | testIds |
      | not setted | 2          | 8,7,5   |
      | not setted | 0          | 10,9,8  |
      | not setted | 5          | 4,2,1   |
      | not setted | 6          | 2,1     |

    Examples: Just numTweets param is setted
      | numTweets  | index      | testIds          |
      | 3          | not setted | 10,9,8           |
      | 6          | not setted | 10,9,8,7,5,4     |
      | 8          | not setted | 10,9,8,7,5,4,2,1 |
      | 9          | not setted | 10,9,8,7,5,4,2,1 |

    Examples: Both params are setted
      | numTweets  | index      | testIds |
      | 3          | 1          | 9,8,7   |



  Scenario: Unauthorised request to get tweets from users the requesting user is following
  The request must contain a valid token

    When a client sends a GET "/tweets" request without a valid token to get a list of tweets
    Then the HTTP response status-code will be 401