Feature: Get users
  This feature file describes the behaviour of the system for GET requests at the endpoint on /api/users to get a list of users
  There should be follwoing behaviour at the system:
  - If the request contains the header "Authorization" with a valid token, then the http response code will be 200 and the body will contain a list of n users, sorted in first grade by their userName, in second grade by their firstName and in third grade by their lastName
  - If the QueryParam "numUsers" is specified, then the number of returned users will be the value of the param "numUsers" if  there are enough matching users stored
  - If the QueryParam "searchString" is specified, then the returned list of users contains just users whos userName, firstName or lastName contains the value of the searchString
  - If the QueryParam "index" is specified, then its value will be the starting index to get the n users from the sorted list of matching users for the response if there are enough matching users persisted
  - If the QueryParam "numUsers" isn´t specified, then its default value will be 5
  - If the QueryParam "searchString" isn´t specified, then its default value will be ''
  - If the request doesn´t contain a valid token, then, if the http response body will be empty and the http response state will be 401


  Background: Persist users
    Given the system has persisted users
      | accountId | userName | firstName | lastName   | role      |
      | 0         | max      | Max       | Mustermann | USER      |
      | 1         | marta    | Marta     | Musterfrau | USER      |
      | 2         | john     | John      | Doe        | USER      |
      | 3         | jane     | Jane      | Doe        | USER      |
      | 4         | werner   | Werner    | Pflanzen   | MODERATOR |
      | 5         | karl     | Karl      | Ranseier   | USER      |
      | 6         | lena     | Lena      | Löchte     | USER      |


  Scenario Outline: Get users
  Requesting n users

    And the <accountType> "<userName>" is authenticated
    When a client sends a "GET" "/users" request for <accountType> "<userName>" to get a list of users
    Then the HTTP response status-code will be 200
    And the HTTP response body contains following JSON with a list of users
      """
      [
      {
          "userId": 0,
          "firstName": "Max",
          "lastName": "Mustermann",
          "role": "USER"
      },
      {
          "userId": 1,
          "firstName": "Marta",
          "lastName": "Musterfrau",
          "role": "USER"
      },
      {
          "userId": 2,
          "firstName": "John",
          "lastName": "Doe",
          "role": "USER"
      },
      {
          "userId": 3,
          "firstName": "Jane",
          "lastName": "Doe",
          "role": "USER"
      },
      {
          "userId": 5,
          "firstName": "Karl",
          "lastName": "Ranseier",
          "role": "USER"
      }
      ]
      """

    Examples:
      | accountType | userName |
      | user        | max    |
      | moderator   | werner |




  Scenario Outline: Returning users

    When a client sends a request to get a list of users with following QueryParams
      | queryParam: | searchString   | numTweets   | index   |
      | value:      | <searchString> | <numTweets> | <index> |
    Then the returned users will be the users with ids <returnedUsers> in presented order

    Examples:
      | searchString | index      | numTweets  | returnedUsers |
      | not setted   | not setted | not setted | 0,1,2,3,5     |
      | not setted   | not setted | 4          | 0,1,2,3       |
      | not setted   | not setted | 6          | 0,1,2,3,5,6   |
      | not setted   | not setted | 7          | 0,1,2,3,5,6   |
      | not setted   | 0          | not setted | 0,1,2,3,5     |
      | not setted   | 1          | not setted | 1,2,3,5,6     |
      | not setted   | 5          | not setted | 6             |
      | not setted   | 6          | not setted |               |
      | not setted   | 2          | 2          | 2,3           |
      | not setted   | 3          | 2          | 3,5           |
      | not setted   | 3          | 3          | 3,5,6         |
      | Mustermann   | not setted | not setted | 0             |
      | mustermann   | not setted | not setted |               |
      | ma           | not setted | not setted | 0,1           |
      | ma           | 1          | not setted | 1             |
      | ma           | not setted | 1          | 0             |



  Scenario: Unauthorised request to get a list of users
  The request must contain a valid token of a user

    When a client sends a request without a valid token to get a list of users
    Then the HTTP response status-code will be 401