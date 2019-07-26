package de.openknowledge.twttrService.api.rest.stepDefinitions;

import cucumber.api.java.en.When;
import de.openknowledge.twttrService.api.rest.supportCode.IntegrationTestUtil;
import de.openknowledge.twttrService.api.rest.supportCode.SharedDomain;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

public class CancelTweetSteps {
    private SharedDomain domain;

    public CancelTweetSteps(SharedDomain domain){
        this.domain = domain;
    }


    @When("a client sends a DELETE {string} request for (user|moderator) {string} to cancel the specified tweet")
    public void a_client_sends_a_DELETE_request_for_user_to_cancel_the_specified_tweet2(String additionalPath, String userName) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(userName))
                .when()
                .delete(IntegrationTestUtil.getBaseURI() + additionalPath);
        domain.setResponse(response);
    }


    @When("a client sends a request for user {string} to cancel the tweet with id {int}")
    public void a_client_sends_a_request_for_user_to_cancel_the_tweet_with_id(String userName, Integer tweetId) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(userName))
                .when()
                .delete(IntegrationTestUtil.getBaseURI() + "/tweets/" + tweetId);
        domain.setResponse(response);
    }


    @When("a client sends a DELETE {string} request without a valid token to cancel the specified tweet")
    public void a_client_sends_a_DELETE_request_without_a_valid_token_to_cancel_the_specified_tweet(String additionalPath) {
        String randomToken = "xxx";
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + randomToken)
                .when()
                .delete(IntegrationTestUtil.getBaseURI() + additionalPath);
        domain.setResponse(response);
    }
}
