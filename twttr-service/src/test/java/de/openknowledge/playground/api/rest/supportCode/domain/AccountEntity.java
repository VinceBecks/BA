package de.openknowledge.playground.api.rest.supportCode.domain;

public class AccountEntity {
    String accountType;
    Integer accountId;
    String userName;
    String firstName;
    String lastName;
    Integer role;

    public AccountEntity(String accountType, Integer accountId, String USERNAME, String firstName, String lastName, Integer role) {
        this.accountType = accountType;
        this.accountId = accountId;
        this.userName = USERNAME;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "ACCOUNT_TYPE='" + accountType + '\'' +
                ", ACCOUNT_ID=" + accountId +
                ", USERNAME='" + userName + '\'' +
                ", FIRST_NAME='" + firstName + '\'' +
                ", LAST_NAME='" + lastName + '\'' +
                ", ROLE=" + role +
                '}';
    }
}
