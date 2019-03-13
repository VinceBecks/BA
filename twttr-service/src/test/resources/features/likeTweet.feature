Feature: Like a tweet
  This feature file describes the behaviour of the system for POST requests at the endpoint on /api/tweets/{tweetId}/liker to like a specified tweet.
  There should be follwoing behaviour at the system:
  - If the request contains the header "Authorization" with a valid token of an user, then the requesting user is a liker of the specified tweet and the http response state will be 204
  - If a user is already a liker of the specified tweet, then he will not be a second time a liker of it
  - If the request doesn´t contain a valid token, then the requesting user isn´t a new liker of the specified tweet and the http response state will be 401
  - If the request contains a valid token which belongs to a moderator, then the specified tweet will not have a new liker and the http response state will be 403
  - If the specified tweet doesn´t exist or is in state "CANCELED", then the http response body will be empty and its state will be 404


  Given the user "max" is authenticated
  And a stored tweet with id 1
  When a client sends a "POST" "/tweets/1/liker" request for user "max" to like the tweet with id 1
  Then the HTTP response state will be 204
