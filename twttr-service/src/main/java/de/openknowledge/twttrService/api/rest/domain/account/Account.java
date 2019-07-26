package de.openknowledge.twttrService.api.rest.domain.account;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "TAB_ACCOUNT")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "ACCOUNT_TYPE")
public class Account implements Serializable {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "ACCOUNT_ID")
    private Integer accountId;

    @Column(name = "USERNAME", nullable = false, unique = true)
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "userName.userName", column = @Column(name = "USERNAME", nullable = false, unique = true)),
            @AttributeOverride(name = "lastName.lastName", column = @Column(name = "LAST_NAME", nullable = false)),
            @AttributeOverride(name = "firstName.firstName", column = @Column(name = "FIRST_NAME", nullable = false))
    })
    private Name name;

    @Column(name = "ROLE", nullable = false)
    private AccountType role;


    Account () {
        //for JPA
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public Name getName() {
        return new Name(this.name.getUserName(), this.name.getFirstName(), this.name.getLastName());
    }

    public AccountType getRole() {
        return role;
    }

    public void setRole(AccountType role) {
        this.role = role;
    }
}