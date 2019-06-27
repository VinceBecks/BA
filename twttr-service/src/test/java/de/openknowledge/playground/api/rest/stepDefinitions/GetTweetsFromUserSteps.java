package de.openknowledge.playground.api.rest.stepDefinitions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import de.openknowledge.playground.api.rest.supportCode.IntegrationTestUtil;
import de.openknowledge.playground.api.rest.supportCode.SharedDomain;
import de.openknowledge.playground.api.rest.supportCode.converter.convertedClasses.GetTweetsQueryParams;
import de.openknowledge.playground.api.rest.supportCode.converter.convertedClasses.TweetDTO;
import de.openknowledge.playground.api.rest.supportCode.converter.convertedClasses.TweetEntity;
import de.openknowledge.playground.api.rest.supportCode.dataSetBuilder.DBConnection;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.keycloak.authorization.client.AuthzClient;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class GetTweetsFromUserSteps {

    private SharedDomain domain;
    public GetTweetsFromUserSteps(SharedDomain domain) {
        this.domain = domain;
    }

    @Given("following tweets got persisted from user john with id 2 in presented order")
    public void following_tweets_got_persisted_from_user_john_with_id_in_presented_order(List<TweetEntity.Builder> tweetBuilders) {
        AtomicLong time = new AtomicLong(System.currentTimeMillis()-10000000);
        List<TweetEntity> tweets = new LinkedList<>();
        tweetBuilders.forEach(builder -> {
            builder.withAuthorId(2);
            builder.withPubDate(new Date(time.addAndGet(1000)));
            tweets.add(builder.build());
        });
        /*tweets.forEach(tweetEntity -> {
            tweetEntity.setPubDate(new Date(time.addAndGet(1000)));
            tweetEntity.setAuthorId(2);
        });*/
        new DBConnection().insertTweets(tweets);
    }

    @When("a client sends a GET {string} request for (user|moderator) {string} to get a list of tweets from user john")
    public void a_client_sends_a_GET_request_for_to_get_a_list_of_tweets_from_user_john(String additionalPath, String userName) {
        String validToken = AuthzClient.create().authorization("max", "password").authorize().getToken();
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + validToken)
                .when()
                .get(IntegrationTestUtil.getBaseURI() + additionalPath);
        domain.setResponse(response);
    }

    @When("a client sends a GET {string} request for user {string} to get a list of tweets from user john with following Query Params")
    public void a_client_sends_a_GET_request_for_user_to_get_a_list_of_tweets_from_user_john_with_following_Query_Params(String additionalPath, String userName, GetTweetsQueryParams params) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(userName))
                .when()
                .get(IntegrationTestUtil.getBaseURI() + additionalPath + params.getQueryString());
        domain.setResponse(response);
    }

    @When("a client sends a GET {string} request without a valid token to get a list of tweets from user john")
    public void a_client_sends_a_GET_request_without_a_valid_token_to_get_a_list_of_tweets_from_user_john(String additionalPath) {
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

    @Then("the HTTP response body will contain following JSON with tweets from user john")
    public void the_HTTP_response_body_will_contain_following_JSON_with_tweets_from_user_john(String expectedJson) {
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
