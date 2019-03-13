package de.openknowledge.playground.api.rest.security.stepDefinition;

import cucumber.api.java.en.When;
import de.openknowledge.playground.api.rest.security.supportCode.SharedDomain;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.keycloak.authorization.client.AuthzClient;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

public class UnlikeTweetSteps {
    private SharedDomain domain;

    public UnlikeTweetSteps (SharedDomain domain) {
        this.domain = domain;
    }

    @When("a client sends a {string} {string} request for user {string} to unlike the tweet with id 1")
    public void a_client_sends_a_request_for_user_to_unlike_the_tweet_with_id(String string, String additionalPath, String userName) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(userName))
                .when()
                .delete(domain.basePath() + additionalPath);
        domain.setResponse(response);
    }

    @When("a client sends a request without a valid token to unlike a specified tweet")
    public void a_client_sends_a_request_without_a_valid_token_to_unlike_a_specified_tweet() {
        String randomToken = "XXX";

        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + randomToken)
                .when()
                .delete(domain.basePath() + "/tweets/1/liker");
        domain.setResponse(response);
    }

    @When("a client sends a request for moderator {string} to unlike a specified tweet")
    public void a_client_sends_a_request_for_moderator_to_unlike_a_specified_tweet(String moderatorName) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(moderatorName))
                .when()
                .delete(domain.basePath() + "/tweets/1/liker");
        domain.setResponse(response);
    }

    @When("a client sends a request to unlike the tweet with id {int}")
    public void a_client_sends_a_request_to_unlike_the_tweet_with_id(Integer tweetId) {
        String validToken = AuthzClient.create().authorization("max", "password").authorize().getToken();
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + validToken)
                .when()
                .delete(domain.basePath() + "/tweets/"+tweetId+"/liker");
        domain.setResponse(response);
    }
}
