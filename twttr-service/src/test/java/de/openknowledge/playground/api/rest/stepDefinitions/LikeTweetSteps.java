package de.openknowledge.playground.api.rest.stepDefinitions;

import cucumber.api.java.en.When;
import de.openknowledge.playground.api.rest.supportCode.IntegrationTestUtil;
import de.openknowledge.playground.api.rest.supportCode.SharedDomain;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

public class LikeTweetSteps {

    private SharedDomain domain;
    public LikeTweetSteps (SharedDomain domain) {
        this.domain = domain;
    }


    @When("a client sends a POST {string} request for (user|moderator) {string} to like the specified tweet")
    public void a_client_sends_a_POST_request_for_user_to_like_the_specified_tweet(String additionalPath, String userName) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(userName))
                .when()
                .post(IntegrationTestUtil.getBaseURI() + additionalPath);
        domain.setResponse(response);
    }


    @When("a client sends a POST {string} request without a valid token to like the specified tweet")
    public void a_client_sends_a_POST_request_without_a_valid_token_to_like_the_specified_tweet(String additionalPath) {
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

    @When("a client sends a request for moderator {string} to like a specified tweet")
    public void a_client_sends_a_request_for_moderator_to_like_a_specified_tweet(String moderatorName) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(moderatorName))
                .when()
                .post(IntegrationTestUtil.getBaseURI() + "/tweets/1/liker");
        domain.setResponse(response);
    }
}
