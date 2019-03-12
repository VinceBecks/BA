Feature: Get token from account

  Scenario Outline: Authenticate

    Given following <accountType>
      | userName   | password |
      | <userName> | <password> |
    When a client sends a POST request to /auth/realms/twttr/protocol/openid-connect/token to get a valid token for "<userName>"
    Then the response body should contain a valid token for the account of "<userName>"

    Examples:
    | accountType | userName | password |
    | user        | max      | password |
    | user        | marta    | password |
    | user        | john     | password |
    | user        | jane     | password |
    | moderator   | jane     | password |
    | user        | werner   | password |
    | user        | karl     | password |
    | user        | lena     | password |
    | user        | bla      | password |
