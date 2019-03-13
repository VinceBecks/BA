package de.openknowledge.playground.api.rest.security.stepDefinition;

import cucumber.api.java.en.Given;
import de.openknowledge.playground.api.rest.security.supportCode.SharedDomain;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.representations.idm.authorization.AuthorizationResponse;

public class AccountSteps {

    private SharedDomain domain;

    public AccountSteps (SharedDomain domain) {
        this.domain = domain;
    }

    @Given("the user {string} is authenticated")
    public void the_user_is_authenticated(String userName) {
        AuthorizationResponse response = AuthzClient.create().authorization(userName, domain.getAccount(userName).getPassword()).authorize();
        domain.addValidToken(userName, response.getToken());
    }

    @Given("the moderator {string} is authenticated")
    public void the_moderator_is_authenticated(String moderatorName) {
        AuthorizationResponse response = AuthzClient.create().authorization(moderatorName, domain.getAccount(moderatorName).getPassword()).authorize();
        domain.addValidToken(moderatorName, response.getToken());
    }
}
