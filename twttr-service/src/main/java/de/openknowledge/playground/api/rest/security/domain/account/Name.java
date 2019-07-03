package de.openknowledge.playground.api.rest.security.domain.account;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
public class Name {

    @Embedded
    private UserName userName;

    @Embedded
    private FirstName firstName;

    @Embedded
    private LastName lastName;

    public Name() {
        //for JPA
    }

    public Name(UserName userName, FirstName firstName, LastName lastName) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public UserName getUserName() {
        return new UserName(this.userName.getUserName());
    }

    public void setUserName(UserName userName) {
        this.userName = userName;
    }

    public FirstName getFirstName() {
        return new FirstName(this.firstName.getFirstName());
    }

    public void setFirstName(FirstName firstName) {
        this.firstName = firstName;
    }

    public LastName getLastName() {
        return new LastName(this.lastName.getLastName());
    }

    public void setLastName(LastName lastName) {
        this.lastName = lastName;
    }
}
