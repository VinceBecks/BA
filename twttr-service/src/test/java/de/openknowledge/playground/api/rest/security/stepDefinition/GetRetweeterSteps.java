package de.openknowledge.playground.api.rest.security.stepDefinition;

import cucumber.api.PendingException;
import cucumber.api.java.en.When;
import de.openknowledge.playground.api.rest.security.supportCode.IntegrationTestUtil;
import de.openknowledge.playground.api.rest.security.supportCode.SharedDomain;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.keycloak.authorization.client.AuthzClient;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

public class GetRetweeterSteps {
    private SharedDomain domain;

    public GetRetweeterSteps (SharedDomain domain ) {
        this.domain = domain;
    }


    @When("a client sends a GET {string} request for user {string} to get a list of retweeter of the tweet with id 1")
    public void a_client_sends_a_GET_request_for_user_to_get_a_list_of_retweeter_of_the_tweet_with_id(String additionalPath, String userName) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(userName))
                .when()
                .get(IntegrationTestUtil.getBaseURI() + additionalPath);
        domain.setResponse(response);
    }


    @When("a client sends a request without a valid token of an user to get a list of retweeter of a tweet")
    public void a_client_sends_a_request_without_a_valid_token_of_an_user_to_get_a_list_of_retweeter_of_a_tweet() {
        String randomToken = "XXX";

        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + randomToken)
                .when()
                .get(IntegrationTestUtil.getBaseURI() + "/tweets/1/retweets/authors");
        domain.setResponse(response);
    }

    @When("a client sends a request for moderator {string} to get a list of retweeter of a tweet")
    public void a_client_sends_a_request_for_moderator_to_get_a_list_of_retweeter_of_a_tweet(String moderatorName) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(moderatorName))
                .when()
                .get(IntegrationTestUtil.getBaseURI() + "/tweets/1/retweets/authors");
        domain.setResponse(response);
    }

    @When("a client sends a request to get a list of retweeter of the tweet with id {int}")
    public void a_client_sends_a_request_to_get_a_list_of_retweeter_of_the_tweet_with_id(Integer tweetId) {
        String validToken = AuthzClient.create().authorization("max", "password").authorize().getToken();

        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + validToken)
                .when()
                .get(IntegrationTestUtil.getBaseURI() + "/tweets/"+tweetId+"/retweets/authors");
        domain.setResponse(response);
    }


    @When("a client sends a request to get a list of retweeter from the tweet with id {int}")
    public void a_client_sends_a_request_to_get_a_list_of_retweeter_from_the_tweet_with_id(Integer tweetId) {
        String validToken = AuthzClient.create().authorization("max", "password").authorize().getToken();

        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + validToken)
                .when()
                .get(IntegrationTestUtil.getBaseURI() + "/tweets/"+tweetId+"/retweets/authors");
        domain.setResponse(response);
    }
}
