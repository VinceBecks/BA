package de.openknowledge.twttrService.api.rest.application.user;

import de.openknowledge.twttrService.api.rest.domain.account.AccountType;
import de.openknowledge.twttrService.api.rest.domain.account.User;

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
        this.firstName = user.getName().getFirstName().getFirstName();
        this.lastName = user.getName().getLastName().getLastName();
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
