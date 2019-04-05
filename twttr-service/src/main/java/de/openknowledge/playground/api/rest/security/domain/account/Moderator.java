package de.openknowledge.playground.api.rest.security.domain.account;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("MODERATOR")
public class Moderator extends Account{

    public Moderator () {
        // for JPA
    }

    public Moderator (String firstName, String lastName, String userName) {
        super (firstName, lastName, userName);
        setRole(AccountType.MODERATOR);
    }
}
