package de.openknowledge.playground.api.rest.stepDefinitions;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import de.openknowledge.playground.api.rest.supportCode.IntegrationTestUtil;
import de.openknowledge.playground.api.rest.supportCode.SharedDomain;
import de.openknowledge.playground.api.rest.supportCode.converter.convertedClasses.TweetDTO;
import de.openknowledge.playground.api.rest.supportCode.converter.convertedClasses.TweetEntity;
import de.openknowledge.playground.api.rest.supportCode.dataBase.DBConnection;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.Matchers;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class RetweetTweetSteps {

    private SharedDomain domain;
    public RetweetTweetSteps(SharedDomain domain) {
        this.domain = domain;
    }

    //todo: sollte ggf. heiteßn "a stored retweet with id 2 OF AN tweet with id 1   ---> der andere Step kann auch mal vorkommen, wo andere Steps den Tweet mit id 1 bereits manipuliert haben
    @Given("a stored retweet with id 2 from tweet with id 1")
    public void a_stored_retweet_with_id_from_tweet_with_id() {
        List<TweetEntity> tweets = new LinkedList<>();

        TweetEntity entity = new TweetEntity(1, "Example content", new Date(System.currentTimeMillis()),0, 0);
        tweets.add(entity);

        entity = new TweetEntity(2, "Example content", new Date(System.currentTimeMillis()),0, 1);
        entity.setRootTweetId(1);
        tweets.add(entity);

        new DBConnection().insertTweets(tweets);
    }


    @When("a client sends a POST {string} request for (user|moderator) {string} to retweet the specified (tweet|retweet)")
    public void a_client_sends_a_POST_request_for_user_to_retweet_the_specified_tweet(String additionalPath, String userName) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(userName))
                .when()
                .post(IntegrationTestUtil.getBaseURI() + additionalPath);
        domain.setResponse(response);
    }

    @When("a client sends a POST {string} request without a valid token to retweet the specified tweet")
    public void a_client_sends_a_POST_request_without_a_valid_token_to_retweet_the_specified_tweet(String additionalPath) {
        String randomToken = "XXX";

        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + randomToken)
                .when()
                .post(IntegrationTestUtil.getBaseURI() + additionalPath);
        domain.setResponse(response);
    }


    @Then("the HTTP response body contains following JSON of a new retweet from tweet with id {int}, while the tweetId and the publish-date got generated by the system")
    public void the_HTTP_response_body_contains_following_JSON_of_a_new_retweet_from_tweet_with_id_while_the_tweetId_and_the_publish_date_got_generated_by_the_system(Integer rootTweetId, TweetDTO expectedTweet) {
        domain.getResponse().then()
                .contentType(MediaType.APPLICATION_JSON)
                .body("content", Matchers.equalTo(expectedTweet.getContent()))
                .body("author.firstName", Matchers.equalTo(expectedTweet.getAuthor().getFirstName()))
                .body("author.lastName", Matchers.equalTo(expectedTweet.getAuthor().getLastName()))
                .body("author.role", Matchers.equalTo(expectedTweet.getAuthor().getRole().toString()))
                .body("rootTweet.tweetId", Matchers.equalTo(expectedTweet.getRootTweet().getTweetId()));
        //todo: reicht das auch als überprüfung, ob es sich beim Root Tweet um den richtigen handelt?
    }
}
