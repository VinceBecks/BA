package de.openknowledge.playground.api.rest.security.stepDefinition;

import cucumber.api.java.en.When;
import de.openknowledge.playground.api.rest.security.supportCode.SharedDomain;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

public class GetTweetDetailsSteps {
    private SharedDomain domain;

    public GetTweetDetailsSteps (SharedDomain domain) {
        this.domain = domain;
    }


    @When("a client sends a {string} {string} request for user {string} to get detailed information about the tweet with id {int}")
    public void a_client_sends_a_request_for_user_to_get_detailed_information_about_the_tweet_with_id(String httpMethod, String additionalPath, String userName, Integer tweetId) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(userName))
                .when()
                .get(domain.basePath() + additionalPath);
        domain.setResponse(response);

    }
}
