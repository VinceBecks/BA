package de.openknowledge.playground.api.rest.security.stepDefinition;

import cucumber.api.java.en.When;
import de.openknowledge.playground.api.rest.security.domain.tweet.NewTweet;
import de.openknowledge.playground.api.rest.security.supportCode.IntegrationTestUtil;
import de.openknowledge.playground.api.rest.security.supportCode.SharedDomain;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

public class CreateTweetSteps {

    private SharedDomain domain;

    public CreateTweetSteps (SharedDomain domain) {
        this.domain = domain;
    }

    @When("a client sends a POST {string} request for the user {string} to create a new tweet and the body contains following JSON")
    public void a_client_sends_a_POST_request_for_the_user_to_create_a_new_tweet_and_the_body_contains_following_JSON(String additionalPath, String userName, NewTweet newTweet) {
        Response response = RestAssured
                .given()
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(userName))
                    .body(newTweet)
                    .when()
                    .post(IntegrationTestUtil.getBaseURI() + additionalPath);
        domain.setResponse(response);
    }

    @When("a client sends a request for {string} to create a new tweet with {int} characters")
    public void a_client_sends_a_request_for_max_to_create_a_new_tweet_with_characters(String userName, Integer numberCharacters) {
        StringBuilder buffer = new StringBuilder(numberCharacters);
        for (int i = 0; i < numberCharacters; i++) {
            buffer.append("x");
        }
        NewTweet newTweet = new NewTweet(buffer.toString());

        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(userName))
                .body(newTweet)
                .when()
                .post(IntegrationTestUtil.getBaseURI() + "/tweets");
        domain.setResponse(response);
    }

    @When("a client sends a request to create a new tweet without a valid token of an user")
    public void a_client_sends_a_request_to_create_a_new_tweet_without_a_valid_token_of_an_user() {
        NewTweet newTweet = new NewTweet("An example content");
        String randomToken = "xxx";

        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + randomToken)
                .body(newTweet)
                .when()
                .post(IntegrationTestUtil.getBaseURI() + "/tweets");
        domain.setResponse(response);
    }

    @When("a client sends a request to create a tweet for the moderator {string}")
    public void a_client_sends_a_request_to_create_a_tweet_for_the_moderator(String moderatorName) {
        NewTweet newTweet = new NewTweet("An example content");
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(moderatorName))
                .body(newTweet)
                .when()
                .post(IntegrationTestUtil.getBaseURI() + "/tweets");
        domain.setResponse(response);
    }
}
