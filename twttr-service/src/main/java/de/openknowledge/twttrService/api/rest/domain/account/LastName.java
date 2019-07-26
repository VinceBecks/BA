package de.openknowledge.twttrService.api.rest.domain.account;

import javax.persistence.Embeddable;

@Embeddable
public class LastName {
    private String lastName;

    public LastName() {
        //for JPA
    }

    public LastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
