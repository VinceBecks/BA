package de.openknowledge.twttrService.api.rest.stepDefinitions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import de.openknowledge.twttrService.api.rest.supportCode.IntegrationTestUtil;
import de.openknowledge.twttrService.api.rest.supportCode.SharedDomain;
import de.openknowledge.twttrService.api.rest.supportCode.domain.GetTweetsQueryParams;
import de.openknowledge.twttrService.api.rest.supportCode.domain.TweetDTO;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.Matchers;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

public class GetTweetsSteps {

    private SharedDomain domain;
    public GetTweetsSteps (SharedDomain domain) {
        this.domain = domain;
    }

    @When("a client sends a GET {string} request for (user|moderator) {string} to get a list of tweets")
    public void a_client_sends_a_GET_request_for_to_get_a_list_of_tweets_from_users_he_follows(String additionalPath, String userName) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(userName))
                .when()
                .get(IntegrationTestUtil.getBaseURI() + additionalPath);
        domain.setResponse(response);
    }


    @When("a client sends a GET {string} request for (user|moderator) {string} to get a list of tweets with following Query Params")
    public void a_client_sends_a_GET_request_user_to_get_a_list_of_tweets_from_users_he_follows_with_following_Query_Params(String additionalPath, String userName, GetTweetsQueryParams params) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(userName))
                .when()
                .get(IntegrationTestUtil.getBaseURI() + additionalPath + params.getQueryString());
        domain.setResponse(response);
    }




    @When("a client sends a GET {string} request without a valid token to get a list of tweets")
    public void a_client_sends_a_GET_request_without_a_valid_token_to_get_a_list_of_tweets(String additionalPath) {
        String randomToken = "XXX";

        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + randomToken)
                .when()
                .get(IntegrationTestUtil.getBaseURI() + additionalPath);
        domain.setResponse(response);
    }

    @Then("the HTTP response body will contain following JSON with tweets from users max is following")
    public void the_HTTP_response_body_will_contain_following_JSON_with_tweets_from_users_max_is_following(String expectedJson) {
        List<TweetDTO> expectedTweets = null;
        try {
            expectedTweets = new ObjectMapper().readValue(expectedJson, new TypeReference<List<TweetDTO>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
        domain.getResponse().then()
                .contentType(MediaType.APPLICATION_JSON)
                .body("size()", Matchers.equalTo(expectedTweets.size()));

        for (int i=0; i<expectedTweets.size(); i++) {
            domain.getResponse().then()
                    .body("["+i+"].tweetId", Matchers.equalTo(expectedTweets.get(i).getTweetId()))
                    .body("["+i+"].content", Matchers.equalTo(expectedTweets.get(i).getContent()))
                    .body("["+i+"].author.userId", Matchers.equalTo(expectedTweets.get(i).getAuthor().getUserId()))
                    .body("["+i+"].author.firstName", Matchers.equalTo(expectedTweets.get(i).getAuthor().getFirstName()))
                    .body("["+i+"].author.lastName", Matchers.equalTo(expectedTweets.get(i).getAuthor().getLastName()))
                    .body("["+i+"].rootTweet", Matchers.equalTo(expectedTweets.get(i).getRootTweet()));
        }
    }
}
