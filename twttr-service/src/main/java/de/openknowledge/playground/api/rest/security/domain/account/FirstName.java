package de.openknowledge.playground.api.rest.security.domain.account;

import javax.persistence.Embeddable;

@Embeddable
public class FirstName {
    private String firstName;

    public FirstName() {
        //for JPA
    }

    public FirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
