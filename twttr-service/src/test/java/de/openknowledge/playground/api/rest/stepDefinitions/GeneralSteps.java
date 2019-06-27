package de.openknowledge.playground.api.rest.stepDefinitions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import cucumber.api.PendingException;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import de.openknowledge.playground.api.rest.supportCode.IntegrationTestUtil;
import de.openknowledge.playground.api.rest.supportCode.SharedDomain;
import de.openknowledge.playground.api.rest.supportCode.converter.convertedClasses.*;
import de.openknowledge.playground.api.rest.supportCode.dataSetBuilder.DBConnection;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.representations.idm.authorization.AuthorizationResponse;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class GeneralSteps  {

    public SharedDomain domain; //todo: noch private machen?
    public GeneralSteps(SharedDomain domain) {
        this.domain = domain;
    }

    @Before
    public void init() {
        new DBConnection().clearTables();
    }

    @Given("the (user|moderator) {string} is authenticated")
    public void the_user_is_authenticated(String userName) {
        AuthorizationResponse response = AuthzClient.create().authorization(userName, domain.getAccount(userName).getPassword()).authorize();
        domain.addValidToken(userName, response.getToken());
    }

    @Given("user max follows the users john and jane")
    public void user_max_follows_the_users_john_and_jane() {
        List<FollowerEntity> follower = new LinkedList<>();
        follower.add(new FollowerEntity(0, 2));
        follower.add(new FollowerEntity(0, 3));
        new DBConnection().insertFollower(follower);
    }

    @Given("a stored tweet with id 1")
    public void a_stored_tweet_with_id() {
        TweetEntity entity = new TweetEntity(1, "Example content", new Date(System.currentTimeMillis()),0, 0);
        List<TweetEntity> tweets = new LinkedList<>();
        tweets.add(entity);

        new DBConnection().insertTweets(tweets);
    }

    @Given("a stored tweet with id 1 from user max")
    public void a_stored_tweet_with_id_from_user() {
        TweetEntity entity = new TweetEntity(1, "Example content", new Date(System.currentTimeMillis()),0, 0);
        List<TweetEntity> tweets = new LinkedList<>();
        tweets.add(entity);

        new DBConnection().insertTweets(tweets);
    }

    @Given("following tweets got persisted in presented order")
    public void following_tweets_got_persisted_in_presented_order(List<TweetEntity> tweets) {
        AtomicLong time = new AtomicLong(System.currentTimeMillis()-10000000);
        tweets.forEach(tweetEntity -> {
            tweetEntity.setPubDate(new Date(time.addAndGet(1000)));
        });
        new DBConnection().insertTweets(tweets);
    }

    @Given("there is no tweet with id 9999")
    public void there_is_no_tweet_with_id() {
        //todo: muss hier eigentlich was passieren? --> Szenario müsste Step beschreibene, der den speichert und dann in diesem wieder löschen

    }

    @Given("a stored tweet with id 1 in status CANCELED from user max")
    public void a_stored_tweet_with_id_in_status_CANCELED_from_user() {
        TweetEntity entity = new TweetEntity(1, "Example content", new Date(System.currentTimeMillis()),1, 0);
        List<TweetEntity> tweets = new LinkedList<>();
        tweets.add(entity);

        new DBConnection().insertTweets(tweets);
    }

    @Given("the user max is a liker of tweet with id 1")
    public void the_user_max_is_a_liker_of_tweet_with_id() {
        List<LikeEntity> likes = new LinkedList<>();
        likes.add(new LikeEntity(1,0));
        new DBConnection().insertLikes(likes);
    }

    @Given("the user max is not a liker of the tweet with id 1")
    public void the_user_max_is_not_a_liker_of_tweet_with_id() {
        //todo: muss hier irgendwas passieren?
        List<LikeEntity> likes = new LinkedList<>();
        new DBConnection().insertLikes(likes);
    }

    @Given("the tweet with id 1 has one liker and two retweets")
    public void the_tweet_with_id_1_has_one_liker_and_two_retweets() {
        List<TweetEntity> tweets = new LinkedList<>();

        TweetEntity entity = new TweetEntity(2, "Example content", new Date(System.currentTimeMillis()),0, 0);
        entity.setRootTweetId(1);
        tweets.add(entity);

        entity = new TweetEntity(3, "Example content", new Date(System.currentTimeMillis()),0, 2);
        entity.setRootTweetId(1);
        tweets.add(entity);

        DBConnection connection = new DBConnection();
        connection.insertTweets(tweets);

        List<LikeEntity> likes = new LinkedList<>();
        likes.add(new LikeEntity(1, 0));
        connection.insertLikes(likes);
    }

    @When("a client sends a {string} {string} request for user {string}")
    public void a_client_sends_a_request_for_user(String httpMethod, String additionalPath, String userName) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(userName))
                .when()
                .delete(IntegrationTestUtil.getBaseURI() + additionalPath);
        domain.setResponse(response);
    }


    @Then("the HTTP response body contains following JSON of an error message:")
    public void the_HTTP_response_body_contains_following_JSON_of_an_error_message(ErrorMessage expectedErrorMessage) {
        domain.getResponse().then()
                .contentType(MediaType.APPLICATION_JSON)
                .body("errorMessage", Matchers.equalTo(expectedErrorMessage.getErrorMessage()));
    }

    @Then("the HTTP response status-code will be {int}")
    public void the_HTTP_response_status_code_will_be(Integer expectedStatusCode) {
        Response response = domain.getResponse();
        response.then()
                .statusCode(expectedStatusCode);
    }

    //fürBA: Eine Methode, die quasi immer das gleiche macht, und auf die 3 verschiedene Steps gemapped werden
    @Then("the HTTP response body will contain following JSON with a list of users who (liked the stored tweet|retweeted the stored tweet|follows the user john)")
    public void the_HTTP_response_body_will_contain_following_JSON_with_a_list_of_users_who_retweeted_the_stored_tweet(String expectedJson) {
        try {
            List<UserDTO> users = new ObjectMapper().readValue(expectedJson, new TypeReference<List<UserDTO>>() {});
            domain.getResponse().then()
                    .body("size()", Matchers.equalTo(users.size()));

            for (int i=0; i<users.size(); i++) {
                domain.getResponse().then()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("["+i+"].userId", Matchers.equalTo(users.get(i).getUserId()))
                        .body("["+i+"].firstName", Matchers.equalTo(users.get(i).getFirstName()))
                        .body("["+i+"].lastName", Matchers.equalTo(users.get(i).getLastName()))
                        .body("["+i+"].role", Matchers.equalTo(users.get(i).getRole().toString()));
            }

        } catch (IOException e) {
            throw new PendingException();
        }
    }

    @Then("the HTTP response body contains the tweets with the ids {Ids}")
    public void the_HTTP_response_body_contains_the_tweets_with_the_ids(IntegerList ids) {
        List<Integer> expectedTweetIds  = ids.getIntegerList();

        domain.getResponse().then()
                .contentType(MediaType.APPLICATION_JSON)
                .body("size()", Matchers.equalTo(expectedTweetIds.size()));

        for (int i=0; i<expectedTweetIds.size(); i++) {
            domain.getResponse().then()
                    .body("["+i+"].tweetId", Matchers.equalTo(expectedTweetIds.get(i)));
        }
    }


}
