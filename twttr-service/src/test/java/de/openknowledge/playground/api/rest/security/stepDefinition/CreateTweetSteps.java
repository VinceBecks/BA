package de.openknowledge.playground.api.rest.security.stepDefinition;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import de.openknowledge.playground.api.rest.security.domain.tweet.NewTweet;
import de.openknowledge.playground.api.rest.security.domain.tweet.TweetDTO;
import de.openknowledge.playground.api.rest.security.supportCode.SharedDomain;
import de.openknowledge.playground.api.rest.security.supportCode.converter.convertedClasses.ErrorMessage;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.Matchers;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

public class CreateTweetSteps {

    private SharedDomain domain;

    public CreateTweetSteps (SharedDomain domain) {
        this.domain = domain;
    }

    @When("a client sends a {string} {string} request for the user {string} to create a new tweet and the body contains following JSON structure")
    public void a_client_sends_a_request_for_the_user_to_create_a_new_tweet_and_the_body_contains_following_JSON_structure(String httpMethod, String additionalPath, String userName, NewTweet newTweet) {
        Response response = RestAssured
                .given()
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromUser(userName))
                    .body(newTweet)
                    .when()
                    .post(domain.basePath() + additionalPath);
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
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromUser(userName))
                .body(newTweet)
                .when()
                .post(domain.basePath() + "/tweets");
        domain.setResponse(response);
    }

}
