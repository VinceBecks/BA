package de.openknowledge.playground.api.rest.stepDefinition;

import cucumber.api.java.en.When;
import de.openknowledge.playground.api.rest.supportCode.IntegrationTestUtil;
import de.openknowledge.playground.api.rest.supportCode.SharedDomain;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.keycloak.authorization.client.AuthzClient;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

public class UnfollowUserSteps {
    private SharedDomain domain;
    public UnfollowUserSteps (SharedDomain domain) {
        this.domain =domain;
    }


    @When("a client sends a DELETE {string} request for user {string} to unfollow user john")
    public void a_client_sends_a_DELETE_request_for_user_to_unfollow_user_john(String additionalPath, String userName) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(userName))
                .when()
                .delete(IntegrationTestUtil.getBaseURI() + additionalPath);
        domain.setResponse(response);
    }


    @When("a client sends a request to unfollow a specified user without a valid token of an user")
    public void a_client_sends_a_request_to_unfollow_a_specified_user_without_a_valid_token_of_an_user() {
        String randomToken = "XXX";

        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + randomToken)
                .when()
                .delete(IntegrationTestUtil.getBaseURI() + "/users/1/follower");
        domain.setResponse(response);
    }

    @When("a client sends a request for moderator {string} to unfollow a specified user")
    public void a_client_sends_a_request_for_moderator_to_unfollow_a_specified_user(String moderatorName) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(moderatorName))
                .when()
                .delete(IntegrationTestUtil.getBaseURI() + "/users/1/follower");
        domain.setResponse(response);
    }

    @When("a client sends a request to unfollow the account of a moderator")
    public void a_client_sends_a_request_to_unfollow_the_account_of_a_moderator() {
        String validToken = AuthzClient.create().authorization("max", "password").authorize().getToken();
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + validToken)
                .when()
                .delete(IntegrationTestUtil.getBaseURI() + "/users/4/follower");
        domain.setResponse(response);
    }

    @When("a client sends a request to unfollow the user with id {int}")
    public void a_client_sends_a_request_to_unfollow_the_user_with_id(Integer userId) {
        String validToken = AuthzClient.create().authorization("max", "password").authorize().getToken();
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + validToken)
                .when()
                .delete(IntegrationTestUtil.getBaseURI() + "/users/"+userId+"/follower");
        domain.setResponse(response);
    }
}
