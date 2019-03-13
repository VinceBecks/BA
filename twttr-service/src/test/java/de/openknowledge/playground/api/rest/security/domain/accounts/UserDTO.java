package de.openknowledge.playground.api.rest.security.domain.accounts;

import de.openknowledge.playground.api.rest.security.domain.account.AccountType;
import de.openknowledge.playground.api.rest.security.domain.account.User;

import java.io.Serializable;

public class UserDTO implements Serializable {
    private Integer userId;
    private String firstName, lastName;
    private AccountType role;

    private UserDTO () {
        //for JPA
    }

    public UserDTO(Integer userId, String firstName, String lastName, AccountType role) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    public UserDTO(User user) {
        this.userId = user.getAccountId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.role = user.getRole();
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public AccountType getRole() {
        return role;
    }

    public void setRole(AccountType role) {
        this.role = role;
    }


    @Override
    public String toString() {
        return "UserDTO{" +
                "userId=" + userId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", role=" + role +
                '}';
    }
}
