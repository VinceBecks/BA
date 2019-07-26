package de.openknowledge.twttrService.api.rest.stepDefinitions;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import de.openknowledge.twttrService.api.rest.supportCode.IntegrationTestUtil;
import de.openknowledge.twttrService.api.rest.supportCode.SharedDomain;
import de.openknowledge.twttrService.api.rest.supportCode.dataBase.DBConnection;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

public class UnfollowUserSteps {

    private SharedDomain domain;
    public UnfollowUserSteps (SharedDomain domain) {
        this.domain = domain;
    }

    @Given("the user max isn´t a follower of user john with id 2")
    public void the_user_isn_t_a_follower_of_user_with_id() {
        if (DBConnection.isUserAFollower(0, 2)){
            throw new IllegalStateException("Max is a follower of user john");
        }
    }


    @When("a client sends a DELETE {string} request for (user|moderator) {string} to unfollow user john")
    public void a_client_sends_a_DELETE_request_for_moderator_to_unfollow_user_john(String additionalPath, String userName) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(userName))
                .when()
                .delete(IntegrationTestUtil.getBaseURI() + additionalPath);
        domain.setResponse(response);
    }

    @When("a client sends a DELETE {string} request without a valid token to unfollow user john")
    public void a_client_sends_a_DELETE_request_without_a_valid_token_to_unfollow_user_john(String additionalPath) {
        String randomToken = "XXX";

        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + randomToken)
                .when()
                .delete(IntegrationTestUtil.getBaseURI() + additionalPath);
        domain.setResponse(response);
    }


    @When("a client sends a DELETE {string} request for user {string} to unfollow a moderator")
    public void a_client_sends_a_DELETE_request_for_user_to_unfollow_a_moderator(String additionalPath, String userName) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(userName))
                .when()
                .delete(IntegrationTestUtil.getBaseURI() + additionalPath);
        domain.setResponse(response);
    }

    @When("a client sends a DELETE {string} request for user {string} to unfollow the specified user")
    public void a_client_sends_a_DELETE_request_for_user_to_unfollow_the_specified_user(String additionalPath, String userName) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(userName))
                .when()
                .delete(IntegrationTestUtil.getBaseURI() + additionalPath);
        domain.setResponse(response);
    }
}
