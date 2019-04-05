package de.openknowledge.playground.api.rest.security.application.user;

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
}
