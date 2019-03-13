Feature: Get all Tweets
  This feature file describes the behaviour of the system for GET requests at the endpoint on /api/tweets for receiving a sorted list of the last tweets of users the requesint user is following
  There should be follwoing behaviour at the system:
  - If the request contains the header "Authorization" with a valid token of an user, then the http response body contains a list of the last n tweets in state "ACTIVE" from users the requesting user follows, sorted by the publish date of the tweets and the http response state will be 200
  - If the valid token belongs to a moderator, then the responded n tweets are from all users
  - If the QueryParam "index" of the request is higher than 0, then its value describes how many of the last tweets in state "ACTIVE" will be skipped for the response
  - If the QueryParam "numTweets" is higher than 0, then the response contains as many tweets in state "ACTIVE" as its value, if there are enough
  - If the QueryParam "index" isn´t setted, then its default value will be 0
  - If the QueryParam "numTweets" isn´t setted, then its default value will be 5
  - If the number of requested tweets is higher than the list of tweets, or the difference of the list of tweets and the index to start from, then all remaining tweets will be responded and the http response state will be 200
  - If the QueryParam "index" or the QueryParam "numTweets" isn´t a positive integer, then there will be no tweets responded and the http response will contain an appropriate information and the state will be 400
  - If the request doesn´t contain a valid token, then there will be no tweets responded and the http response state will be 401


  #BAinfo: andersrum würde bei liker nicht gehen, da sonst beim persistieren der tweets die liker wieder überschrieben worden wären
  Background: Persist tweets and let max follow john and jane
    Given the user "max" is authenticated
    And following tweets got persisted in presented order
      | tweetId | content  | state    | author |
      | 1       | 1. tweet | PUBLISH  | john   |
      | 2       | 2. tweet | PUBLISH  | jane   |
      | 3       | 3. tweet | CANCELED | john   |
      | 4       | 4. tweet | PUBLISH  | max    |
      | 5       | 5. tweet | PUBLISH  | john   |
      | 6       | 6. tweet | CANCELED | karl   |
      | 7       | 7. tweet | PUBLISH  | jane   |
      | 7       | 7. tweet | PUBLISH  | lena   |
      | 7       | 7. tweet | PUBLISH  | jane   |
      | 7       | 7. tweet | PUBLISH  | jane   |
    And user max follows the users john and jane






  Scenario: Get information about tweets in state ACTIVE from users the requesting user is following
  Requesting tweets with a valid token of an user
  Just tweets from users the requesting user is following and which are in state "ACTIVE" will be responded
  Responded list will be sorted by their publish date

    When a client sends a "GET" "/tweets" request for "max" to get a list of tweets from users he follows
    Then the HTTP response status-code will be 200
    And the HTTP response body will contain following JSON with tweets from users max is following
    """
    [
    {
        "tweetId": 10,
        "content": "10. tweet",
        "pubDate": 1549016291000,
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
        "pubDate": 1549016231000,
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
        "pubDate": 1549016111000,
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



  Scenario Outline: Change QueryParams
    When a client sends a "GET" "/tweets" request user "max" to get a list of tweets from users he follows with following Query Params
      | queryParam: | numTweets   | index   |
      | value:      | <numTweets> | <index> |
    Then the HTTP response status-code will be 200
    And the HTTP response body contains the tweets with the ids <testIds>

    Examples:
      | numTweets  | index      | testIds      |
      | not setted | not setted | 10,9,7       |
      | not setted | 2          | 7,5,2        |
      | not setted | 0          | 10,9,7       |
      | not setted | 5          | 1            |
      | not setted | 6          |              |
      | 3          | not setted | 10,9,7       |
      | 6          | not setted | 10,9,7,5,2,1 |
      | 7          | not setted | 10,9,7,5,2,1 |
      | 3          | 1          | 9,7,5        |




    #todo: zählt das noch als konkretes Beispiel? ... ohne beschreiben, dass es einen user gibt, der aber nicht angemeldet ist?
  Scenario: Unauthorised request to get tweets from users the requesting user is following
  The request must contain a valid token of a user

    When a client sends a request without a valid token to get tweets
    Then the HTTP response status-code will be 401