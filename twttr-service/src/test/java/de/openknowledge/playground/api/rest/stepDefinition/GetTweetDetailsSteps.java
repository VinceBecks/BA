package de.openknowledge.playground.api.rest.stepDefinition;

import cucumber.api.java.en.When;
import de.openknowledge.playground.api.rest.supportCode.IntegrationTestUtil;
import de.openknowledge.playground.api.rest.supportCode.SharedDomain;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.keycloak.authorization.client.AuthzClient;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

public class GetTweetDetailsSteps {
    private SharedDomain domain;

    public GetTweetDetailsSteps (SharedDomain domain) {
        this.domain = domain;
    }


    @When("a client sends a GET {string} request for (user|moderator) {string} to get detailed information about the tweet with id {int}")
    public void a_client_sends_a_GET_request_for_user_to_get_detailed_information_about_the_tweet_with_id(String additionalPath, String userName, Integer tweetId) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(userName))
                .when()
                .get(IntegrationTestUtil.getBaseURI() + additionalPath);
        domain.setResponse(response);

    }

    @When("a client sends a GET {string} request for user {string} to get detailed information about the retweet with id 2")
    public void a_client_sends_a_GET_request_for_user_to_get_detailed_information_about_the_retweet_with_id(String additionalPath, String userName) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(userName))
                .when()
                .get(IntegrationTestUtil.getBaseURI() + additionalPath);
        domain.setResponse(response);
    }


    @When("a client sends a request without a valid token to get detailed information about a tweet")
    public void a_client_sends_a_request_without_a_valid_token_to_get_detailed_information_about_a_tweet() {
        String randomToken = "XXX";

        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + randomToken)
                .when()
                .get(IntegrationTestUtil.getBaseURI() + "/tweets/1");
        domain.setResponse(response);
    }


    @When("a client sends a request to get detailed information about the tweet with id {int}")
    public void a_client_sends_a_request_to_get_detailed_information_about_the_tweet_with_id(Integer tweetId) {
        String validToken = AuthzClient.create().authorization("max", "password").authorize().getToken();

        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + validToken)
                .when()
                .get(IntegrationTestUtil.getBaseURI() + "/tweets/" + tweetId);
        domain.setResponse(response);
    }
}
