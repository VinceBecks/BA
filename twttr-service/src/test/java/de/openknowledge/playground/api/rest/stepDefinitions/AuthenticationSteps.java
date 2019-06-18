package de.openknowledge.playground.api.rest.stepDefinitions;

import cucumber.api.java.en.Given;
import de.openknowledge.playground.api.rest.supportCode.SharedDomain;
import io.cucumber.datatable.DataTable;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.representations.idm.authorization.AuthorizationResponse;

import java.util.Map;

public class AuthenticationSteps {

    private SharedDomain domain;
    public AuthenticationSteps (SharedDomain domain) {
        this.domain = domain;
    }



    @Given("the (user|moderator) {string} is authenticated")
    public void the_user_is_authenticated(String userName) {
        AuthorizationResponse response = AuthzClient.create().authorization(userName, domain.getAccount(userName).getPassword()).authorize();
        domain.addValidToken(userName, response.getToken());
    }
}
