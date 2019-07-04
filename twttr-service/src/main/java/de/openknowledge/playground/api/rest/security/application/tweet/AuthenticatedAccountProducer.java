package de.openknowledge.playground.api.rest.security.application.tweet;

import de.openknowledge.playground.api.rest.security.domain.account.Account;
import de.openknowledge.playground.api.rest.security.infrastructure.persistence.repository.TwttrRepository;
import de.openknowledge.playground.api.rest.security.infrastructure.security.Authenticated;
import org.keycloak.KeycloakSecurityContext;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

public class AuthenticatedAccountProducer {
    @Inject
    private TwttrRepository repository;

    @Produces
    @RequestScoped
    @Authenticated
    public Account getUser(final KeycloakSecurityContext context) {
        String userName = context.getToken().getPreferredUsername();
        Account account = repository.findAccountByUserName(userName);
        return account;
    }
}
