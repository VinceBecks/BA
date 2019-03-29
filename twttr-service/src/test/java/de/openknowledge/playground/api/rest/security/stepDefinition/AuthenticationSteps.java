package de.openknowledge.playground.api.rest.security.stepDefinition;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
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
    private String pubKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCrVrCuTtArbgaZzL1hvh0xtL5mc7o0NqPVnYXkLvgcwiC3BjLGw1tGEGoJaXDuSaRllobm53JBhjx33UNv+5z/UMG4kytBWxheNVKnL6GgqlNabMaFfPLPCF8kAgKnsi79NMo+n6KnSY8YeUmec/p2vjO2NjsSAVcWEQMVhJ31LwIDAQAB";


    public AuthenticationSteps (SharedDomain domain) {
        this.domain = domain;
    }


    @Given("following moderator")
    public void following_moderator(DataTable dataTable) {
        Map<String, String> account = dataTable.transpose().asMap(String.class, String.class);
        domain.setPasswordForUser(account.get("userName"), null);
        domain.setPasswordForUser(account.get("userName"), account.get("password"));
    }

    @Given("following user")
    public void following_user(DataTable dataTable) {
        Map<String, String> account = dataTable.transpose().asMap(String.class, String.class);
        domain.setPasswordForUser(account.get("userName"), account.get("password"));
    }


    @When("a client sends a POST request to \\/auth\\/realms\\/twttr\\/protocol\\/openid-connect\\/token to get a valid token for {string}")
    public void a_client_sends_a_POST_request_to_auth_realms_twttr_protocol_openid_connect_token_to_get_a_valid_token_for(String userName) {
        AuthorizationResponse response = AuthzClient.create().authorization(userName, domain.getPasswordFromUser(userName)).authorize();
        domain.addValidToken(userName, response.getToken());
    }

    @When("a client sends a request to get a valid token for user {string} with an incorrect password")
    public void a_client_sends_a_request_to_get_a_valid_token_for_user_max_with_an_incorrect_password(String userName) {
        try {
            AuthorizationResponse response = AuthzClient.create().authorization(userName, "XXX").authorize();
            domain.addValidToken(userName, response.getToken());
        } catch (Exception e) {
            domain.addValidToken(userName, null);
        }
    }

    @When("a client sends a request to get a valid token for user {string} with an incorrect userName")
    public void a_client_sends_a_request_to_get_a_valid_token_for_user_max_with_an_incorrect_userName(String userName) {
        try {
            AuthorizationResponse response = AuthzClient.create().authorization("XXX", domain.getPasswordFromUser(userName)).authorize();
            domain.addValidToken(userName, response.getToken());
        } catch (Exception e) {
            domain.addValidToken(userName, null);
        }
    }

    @When("a client sends a request to get a valid token for user {string} without an userName")
    public void a_client_sends_a_request_to_get_a_valid_token_for_user_max_without_an_userName(String userName) {
        try {
            AuthorizationResponse response = AuthzClient.create().authorization(null, domain.getPasswordFromUser(userName)).authorize();
            domain.addValidToken(userName, response.getToken());
        } catch (Exception e) {
            domain.addValidToken(userName, null);
        }
    }

    @When("a client sends a request to get a valid token for user {string} without an password")
    public void a_client_sends_a_request_to_get_a_valid_token_for_user_without_an_password(String userName) {
        try {
            AuthorizationResponse response = AuthzClient.create().authorization(userName, null).authorize();
            domain.addValidToken(userName, response.getToken());
        } catch (Exception e) {
            domain.addValidToken(userName, null);
        }
    }

    @Then("the response body should contain a valid token for the account of {string}")
    public void the_response_body_should_contain_a_valid_token_for_the_account_of(String userName) {
        //todo: Prüfung reicht nicht .. KeyCloak gibt immer einen Token zurück nur ist dieser nicht immer gültig
        Assert.assertNotNull(domain.tokenFromAccount(userName));
        DecodedJWT jwt = JWT.decode(domain.tokenFromAccount(userName));
        Assert.assertEquals("Username stimmt nicht mit preferred_username überein", userName, jwt.getClaim("preferred_username").asString());
    }

    @Then("there should not be a valid token generated")
    public void there_should_not_be_a_valid_token_generated() {
        Assert.assertNull(domain.tokenFromAccount("max"));
    }

    @Then("the genarated token for user {string} is invalid")
    public void the_genarated_token_for_user_is_invalid(String userName) {
        Assert.assertNotNull(domain.tokenFromAccount(userName));
        DecodedJWT jwt = JWT.decode(domain.tokenFromAccount(userName));
        Assert.assertNotEquals("Username stimmt nicht mit preferred_username überein", userName, jwt.getClaim("preferred_username").asString());
    }
}
