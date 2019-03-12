package de.openknowledge.playground.api.rest.security.stepDefinition;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import de.openknowledge.playground.api.rest.security.domain.tweet.NewTweet;
import de.openknowledge.playground.api.rest.security.domain.tweet.TweetDTO;
import de.openknowledge.playground.api.rest.security.supportCode.SharedDomain;
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

    @Then("the HTTP response state will be {int}")
    public void the_HTTP_response_state_will_be(Integer expectedStatusCode) {
        Response response = domain.getResponse();
        response.then()
                .contentType(MediaType.APPLICATION_JSON)
                .statusCode(expectedStatusCode);
    }

    @Then("the HTTP response body contains following JSON of a new Tweet, while the tweetId and the publish-date got generated by the system")
    public void the_HTTP_response_body_contains_following_JSON_of_a_new_Tweet_while_the_tweetId_and_the_publish_date_got_generated_by_the_system(TweetDTO expectedTweet) {
        domain.getResponse().then()
                .body("content", Matchers.equalTo("An example content"))
                .body("author.firstName", Matchers.equalTo("Max"))
                .body("author.lastName", Matchers.equalTo("Mustermann"))
                .body("author.role", Matchers.equalTo("USER"))
                .body("rootTweet", Matchers.isEmptyOrNullString());
    }
}
