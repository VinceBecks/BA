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



  Scenario: The password is incorrect

    Given following user
      | userName | password |
      | max      | password |
    When a client sends a request to get a valid token for user "max" with an incorrect password
    Then there should not be a valid token generated


  Scenario: The userName is incorrect

    Given following user
      | userName | password |
      | max      | password |
    When a client sends a request to get a valid token for user "max" with an incorrect userName
    Then there should not be a valid token generated


  Scenario: Missing userName

    Given following user
      | userName | password |
      | max      | password |
    When a client sends a request to get a valid token for user "max" without an userName
    Then the genarated token for user "max" is invalid


  Scenario: Missing password

    Given following user
      | userName | password |
      | max      | password |
    When a client sends a request to get a valid token for user "max" without an password
    Then the genarated token for user "max" is invalid