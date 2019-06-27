package de.openknowledge.playground.api.rest.stepDefinitions;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import de.openknowledge.playground.api.rest.supportCode.IntegrationTestUtil;
import de.openknowledge.playground.api.rest.supportCode.SharedDomain;
import de.openknowledge.playground.api.rest.supportCode.converter.convertedClasses.TweetEntity;
import de.openknowledge.playground.api.rest.supportCode.dataBase.DBConnection;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class GetRetweeterSteps {

    private SharedDomain domain;
    public GetRetweeterSteps (SharedDomain domain ) {
        this.domain =domain;
    }

    @Given("an stored tweet with id 1 got retweeted by users max and john")
    public void an_stored_tweet_with_id_got_retweeted_by_users_max_and_john() {
        List<TweetEntity> tweets = new LinkedList<>();

        TweetEntity entity = new TweetEntity(1, "Example content", new Date(System.currentTimeMillis()),0, 1);
        tweets.add(entity);

        entity = new TweetEntity(2, "Example content", new Date(System.currentTimeMillis()),0, 0);
        entity.setAuthorId(0);
        entity.setRootTweetId(1);
        tweets.add(entity);


        entity = new TweetEntity(3, "Example content", new Date(System.currentTimeMillis()),0, 2);
        entity.setRootTweetId(1);
        tweets.add(entity);

        new DBConnection().insertTweets(tweets);
    }



    @When("a client sends a GET {string} request for (user|moderator) {string} to get a list of retweeter of the specified tweet")
    public void a_client_sends_a_GET_request_for_user_to_get_a_list_of_retweeter_of_the_specified_tweet(String additionalPath, String userName) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(userName))
                .when()
                .get(IntegrationTestUtil.getBaseURI() + additionalPath);
        domain.setResponse(response);
    }

    @When("a client sends a GET {string} request without a valid token to get a list of retweeter of the specified tweet")
    public void a_client_sends_a_GET_request_without_a_valid_token_to_get_a_list_of_retweeter_of_the_specified_tweet(String additionalPath) {
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

}
