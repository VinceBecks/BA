Feature: Get users
  This feature file describes the behaviour of the system for GET requests at the endpoint on /api/users to get a list of users
  There should be following behaviour at the system:
  - If the request contains the header "Authorization" with a valid token, then the http response body will contain a list of 3 users, sorted in first grade by their userName, in second grade by their firstName and in third grade by their lastName and the http response status-code will be 200
  - If the QueryParam "numUsers" is specified, then the number of returned users will be the value of the param "numUsers", if there are enough users
  - If the QueryParam "searchString" is specified, then the returned list of users contains just users whos userName, firstName or lastName contains the value of the searchString
  - If the QueryParam "index" is specified, then its value will be the starting index to get the n users from the sorted list of matching users for the response if there are enough matching users persisted
  - If the QueryParam "numUsers" isn´t specified, then its default value will be 5
  - If the QueryParam "searchString" isn´t specified, then its default value will be ''
  - If the request doesn´t contain a valid token, then the http response status-code will be 401

  #fürBA: TypeRegistry hieran vorstellen? Oder lieber an einem Beispiel einer Liste von Tweets? --> nur eine Registrierung möglich (Abfragen, ob Daten gesetzt sind also nötig, wenn unterschiedliche Arten der Initialisierung gewünscht
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




  Scenario: User requests user information
  Requesting users sorted in first grad by their userName,
  in second grade by their firstName and in third grade by their lastName

    And the user "max" is authenticated
    When a client sends a "GET" "/users" request for user "max" to get a list of users
    Then the HTTP response status-code will be 200
    And the HTTP response body contains following JSON with a list of users
      """
      [
          {
              "userId": 3,
              "firstName": "Jane",
              "lastName": "Doe",
              "role": "USER"
          },
          {
              "userId": 2,
              "firstName": "John",
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



  Scenario: Moderator requests user information
  Requesting users sorted in first grad by their userName,
  in second grade by their firstName and in third grade by their lastName

    And the moderator "werner" is authenticated
    When a client sends a "GET" "/users" request for moderator "werner" to get a list of users
    Then the HTTP response status-code will be 200
    And the HTTP response body contains following JSON with a list of users
      """
      [
          {
              "userId": 3,
              "firstName": "Jane",
              "lastName": "Doe",
              "role": "USER"
          },
          {
              "userId": 2,
              "firstName": "John",
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


  Scenario Outline: Change query params for request to get users
  The QueryParam numUsers represents the number of requested users
  The default value for numUsers will be 3
  The QueryParam index represents the number of users to be skipped for the response from a list of users sorted by their username, firstName and lastName in presented order
  The default value for index will be 0
  The QueryParam searchString represents a string which must be containing in the userName, firstName or lastName

    When a client sends a request to get a list of users with following QueryParams
      | queryParam: | searchString   | numTweets   | index   |
      | value:      | <searchString> | <numTweets> | <index> |
    Then the returned users will be the users with ids <returnedUsers> in presented order

      Examples: Without any query parameters setted
        | searchString | index      | numTweets  | returnedUsers |
        | not setted   | not setted | not setted | 3,2,5         |

      Examples: Just numTweets is setted
        | searchString | index      | numTweets  | returnedUsers |
        | not setted   | not setted | 4          | 3,2,5,6       |
        | not setted   | not setted | 6          | 3,2,5,6,1,0   |
        | not setted   | not setted | 7          | 3,2,5,6,1,0   |

      Examples: Just index is setted
        | searchString | index      | numTweets  | returnedUsers |
        | not setted   | 0          | not setted | 3,2,5         |
        | not setted   | 1          | not setted | 2,5,6         |
        | not setted   | 5          | not setted | 0             |
        | not setted   | 6          | not setted |               |

      Examples: Just searchString is setted
        | searchString | index      | numTweets  | returnedUsers |
        | Mustermann   | not setted | not setted | 0             |
        | mustermann   | not setted | not setted |               |
        | ma           | not setted | not setted | 1,0           |

      Examples: Combined query parameters
        | searchString | index      | numTweets  | returnedUsers |
        | ma           | 1          | not setted | 0             |
        | ma           | not setted | 1          | 1             |
        | not setted   | 2          | 2          | 5,6           |
        | not setted   | 3          | 2          | 6,1           |
        | not setted   | 3          | 3          | 6,1,0         |
        | not setted   | 3          | 4          | 6,1,0         |




  Scenario: Unauthorised request to get a list of users
  The request must contain a valid token from an account to get a list of users

    When a client sends a request without a valid token to get a list of users
    Then the HTTP response status-code will be 401

