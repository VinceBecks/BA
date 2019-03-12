package de.openknowledge.playground.api.rest.security.stepDefinition;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import de.openknowledge.playground.api.rest.security.supportCode.SharedDomain;
import io.cucumber.datatable.DataTable;
import org.junit.Assert;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.representations.idm.authorization.AuthorizationResponse;

import java.util.Map;

public class AuthenticationSteps {
    private SharedDomain domain;


    public AuthenticationSteps (SharedDomain domain) {
        this.domain = domain;
    }

    @Given("following moderator")
    public void following_moderator(DataTable dataTable) {
        Map<String, String> account = dataTable.transpose().asMap(String.class, String.class);
        domain.addAccountCredentials(account.get("userName"), account.get("password"));
    }

    @Given("following user")
    public void following_user(DataTable dataTable) {
        Map<String, String> account = dataTable.transpose().asMap(String.class, String.class);
        domain.addAccountCredentials(account.get("userName"), account.get("password"));
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



    @When("a client sends a POST request to \\/auth\\/realms\\/twttr\\/protocol\\/openid-connect\\/token to get a valid token for {string}")
    public void a_client_sends_a_POST_request_to_auth_realms_twttr_protocol_openid_connect_token_to_get_a_valid_token_for(String userName) {
        Map<String, String> account = domain.getAccountCredentials();
        //Generates in create() the required Information from keycloak.json
        AuthorizationResponse response = AuthzClient.create().authorization(account.get(userName), account.get("password")).authorize();
        domain.addValidToken(userName, response.getToken());
    }

    @Then("the response body should contain a valid token for the account of {string}")
    public void the_response_body_should_contain_a_valid_token_for_the_account_of(String userName) {
        //todo: Prüfung reicht nicht .. KeyCloak gibt immer einen Token zurück nur ist dieser nicht immer gültig
        Assert.assertNotNull(domain.tokenFromAccount(userName));
    }


}
