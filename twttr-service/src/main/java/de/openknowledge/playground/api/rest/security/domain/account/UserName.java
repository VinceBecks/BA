package de.openknowledge.playground.api.rest.security.domain.account;

import javax.persistence.Embeddable;

@Embeddable
public class UserName {
    private String userName;

    public UserName() {
        //for JPA
    }

    public UserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
