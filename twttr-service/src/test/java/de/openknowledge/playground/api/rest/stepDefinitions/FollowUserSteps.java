package de.openknowledge.playground.api.rest.stepDefinitions;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import de.openknowledge.playground.api.rest.supportCode.IntegrationTestUtil;
import de.openknowledge.playground.api.rest.supportCode.SharedDomain;
import de.openknowledge.playground.api.rest.supportCode.domain.FollowerEntity;
import de.openknowledge.playground.api.rest.supportCode.dataBase.DBConnection;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.LinkedList;
import java.util.List;

public class FollowUserSteps {

    private SharedDomain domain;
    public FollowUserSteps(SharedDomain domain) {
        this.domain = domain;
    }

    @Given("the user max isn´t a follower of user john with id 2")
    public void the_user_isn_t_a_follower_of_user_with_id() {
        if (new DBConnection().isUserAFollower(0, 2)){
            throw new IllegalStateException("Max is a follower of user john");
        }
    }


    @Given("the user max is a follower of user john with id 2")
    public void the_user_max_is_a_follower_of_user_john_with_id() {
        List<FollowerEntity> follower = new LinkedList<>();
        follower.add(new FollowerEntity(0,2));
        new DBConnection().insertFollower(follower);
    }


    @When("a client sends a POST {string} request for (user|moderator) {string} to follow user john")
    public void a_client_sends_a_POST_request_for_user_to_follow_user_john(String additionalPath, String accountUserName) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(accountUserName))
                .when()
                .post(IntegrationTestUtil.getBaseURI() + additionalPath);
        domain.setResponse(response);
    }

    @When("a client sends a POST {string} request for user max without a valid token to follow user john")
    public void a_client_sends_a_POST_request_for_user_max_without_a_valid_token_to_follow_user_john(String additionalPath) {
        String randomToken = "XXX";
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + randomToken)
                .when()
                .post(IntegrationTestUtil.getBaseURI() + additionalPath);
        domain.setResponse(response);
    }

    @When("a client sends a POST {string} request for user {string} to follow the moderator werner")
    public void a_client_sends_a_POST_request_for_user_to_follow_the_moderator_werner(String additionalPath, String userName) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(userName))
                .when()
                .post(IntegrationTestUtil.getBaseURI() + additionalPath);
        domain.setResponse(response);
    }

    @When("a client sends a POST {string} request for user {string} to follow the account of a moderator")
    public void a_client_sends_a_POST_request_for_user_to_follow_the_account_of_a_moderator(String additionalPath, String userName) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(userName))
                .when()
                .post(IntegrationTestUtil.getBaseURI() + additionalPath);
        domain.setResponse(response);
    }

    @When("a client sends a POST {string} request for user {string} to follow the specified account")
    public void a_client_sends_a_POST_request_for_user_to_follow_the_specified_account(String additionalPath, String userName) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(userName))
                .when()
                .post(IntegrationTestUtil.getBaseURI() + additionalPath);
        domain.setResponse(response);
    }

}
