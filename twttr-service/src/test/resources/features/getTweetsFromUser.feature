Feature: Get all tweets of a member
  This feature file describes the behaviour of the system for GET requests at the endpoint on /api/{userId}/tweets for receiving a sorted list of the last tweets the specified tweet.
  - If the request contains the header "Authorization" with a valid token, then the http response body contains a list of the last n tweets in state "ACTIVE" from the specified user, sorted by the publish date of the tweets and the http response state will be 400
  - If the QueryParam "index" of the request is higher than 0, then its value describes how many of the last tweets in state "ACTIVE" will be skipped for the response
  - If the QueryParam "numTweets" is higher than 0, then the response contains as many tweets in state "ACTIVE" as its value, if there are enough
  - If the QueryParam "index" isn´t setted, then its default value will be 0
  - If the QueryParam "numTweets" isn´t setted, then its default value will be 5
  - If the number of requested tweets is higher than the list of tweets, or the difference of the list of tweets and the index to start from, then all remaining tweets will be responded and the http response state will be 200
  - If the QueryParam "index" or the QueryParam "numTweets" isn´t a positive integer, then there will be no tweets responded and the http response will contain an appropriate information and the state will be 400
  - If the request doesn´t contain a valid token, then there will be no tweets responded and the http response state will be 401


  Background: Authenticate max and persist tweets from john
    Given the user "max" is authenticated
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


    #todo: noch irgendwie auf die Reihenfolge der Tweets eingehen?
  Scenario: Get tweets of a user
    When a client sends a "GET" "/tweets/2" request for "max" to get a list of tweets from user john
    Then the HTTP response status-code will be 200
    And the HTTP response body will contain following JSON with tweets from user john