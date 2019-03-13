package de.openknowledge.playground.api.rest.security.stepDefinition;

import cucumber.api.java.en.When;
import de.openknowledge.playground.api.rest.security.supportCode.SharedDomain;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.keycloak.authorization.client.AuthzClient;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

public class RetweetTweetSteps {
    private SharedDomain domain;

    public RetweetTweetSteps (SharedDomain domain) {
        this.domain = domain;
    }


    @When("a client sends a {string} {string} request for user {string} to retweet the tweet with id 1")
    public void a_client_sends_a_request_for_user_to_retweet_the_tweet_with_id_1(String string, String additionalPath, String userName) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(userName))
                .when()
                .post(domain.basePath() + additionalPath);
        domain.setResponse(response);
    }


    //todo: wäre wieder eine gutes Beispiel, um die Methoden zu verbinden... nur Pattern anpassen, dass es auf beide matched
    @When("a client sends a {string} {string} request for user {string} to retweet the tweet with id 2")
    public void a_client_sends_a_request_for_user_to_retweet_the_tweet_with_id_2(String httpMethod, String additionalPath, String userName) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(userName))
                .when()
                .post(domain.basePath() + additionalPath);
        domain.setResponse(response);
    }


    @When("a client sends a request without a valid token to retweet a specified tweet")
    public void a_client_sends_a_request_without_a_valid_token_to_retweet_a_specified_tweet() {
        String randomToken = "XXX";

        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + randomToken)
                .when()
                .post(domain.basePath() + "/tweets/1/retweets");
        domain.setResponse(response);
    }

    @When("a client sends a request for moderator {string} to retweet a specified tweet")
    public void a_client_sends_a_request_for_moderator_to_retweet_a_specified_tweet(String moderatorName) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(moderatorName))
                .when()
                .post(domain.basePath() + "/tweets/1/retweets");
        domain.setResponse(response);
    }

    @When("a client sends a request to retweet the tweet with id {int}")
    public void a_client_sends_a_request_to_retweet_the_tweet_with_id(Integer tweetId) {
        String validToken = AuthzClient.create().authorization("max", "password").authorize().getToken();
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + validToken)
                .when()
                .post(domain.basePath() + "/tweets/"+tweetId+"/retweets");
        domain.setResponse(response);
    }


}
