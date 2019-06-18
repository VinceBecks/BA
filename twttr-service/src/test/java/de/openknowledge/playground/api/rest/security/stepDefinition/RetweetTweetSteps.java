package de.openknowledge.playground.api.rest.security.stepDefinition;

import com.github.database.rider.core.api.dataset.DataSetExecutor;
import com.github.database.rider.core.configuration.DataSetConfig;
import com.github.database.rider.core.connection.ConnectionHolderImpl;
import com.github.database.rider.core.dataset.DataSetExecutorImpl;
import com.github.database.rider.core.util.EntityManagerProvider;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import de.openknowledge.playground.api.rest.security.supportCode.IntegrationTestUtil;
import de.openknowledge.playground.api.rest.security.supportCode.SharedDomain;
import de.openknowledge.playground.api.rest.security.supportCode.converter.convertedClasses.TweetEntity;
import de.openknowledge.playground.api.rest.security.supportCode.dataSetBuilder.DBSetCreator;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Rule;
import org.keycloak.authorization.client.AuthzClient;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class RetweetTweetSteps {
    private SharedDomain domain;

    @Rule
    EntityManagerProvider entityManagerProvider = EntityManagerProvider.instance("test-local");

    DataSetExecutor dbExecutor;
    @Before
    public void init () {
        this.dbExecutor = DataSetExecutorImpl.instance(new ConnectionHolderImpl(entityManagerProvider.connection()));
    }


    public RetweetTweetSteps (SharedDomain domain) {
        this.domain = domain;
    }



    @Given("a stored retweet with id 2 from tweet with id 1")
    public void a_stored_retweet_with_id_from_tweet_with_id() {
        List<TweetEntity> tweets = new LinkedList<>();

        TweetEntity entity = new TweetEntity(1, "Example content", new Date(System.currentTimeMillis()),0, "max");
        entity.setAuthorId(0);
        tweets.add(entity);

        entity = new TweetEntity(2, "Example content", new Date(System.currentTimeMillis()),0, "max");
        entity.setAuthorId(0);
        entity.setRootTweetId(1);
        tweets.add(entity);

        DBSetCreator creator = new DBSetCreator(dbExecutor);
        creator.createTweetDataSet(new DataSetConfig(""), tweets);
    }


    @When("a client sends a POST {string} request for user {string} to retweet the tweet with id 1")
    public void a_client_sends_a_POST_request_for_user_to_retweet_the_tweet_with_id_1(String additionalPath, String userName) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(userName))
                .when()
                .post(IntegrationTestUtil.getBaseURI() + additionalPath);
        domain.setResponse(response);
    }


    //fürBA: Gutes Bsp. wie man Step Formulierungen verbessern könnte für Entwickler... Einfach aus tweet 1 den Retweet machen, dann kann die gleiche Methode wie die drüber verwendet werden
    @When("a client sends a POST {string} request for user {string} to retweet the tweet with id 2")
    public void a_client_sends_a_POST_request_for_user_to_retweet_the_tweet_with_id_2(String additionalPath, String userName) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(userName))
                .when()
                .post(IntegrationTestUtil.getBaseURI() + additionalPath);
        domain.setResponse(response);
    }


    @When("a client sends a request without a valid token of an user to retweet a specified tweet")
    public void a_client_sends_a_request_without_a_valid_token_of_an_user_to_retweet_a_specified_tweet() {
        String randomToken = "XXX";

        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + randomToken)
                .when()
                .post(IntegrationTestUtil.getBaseURI() + "/tweets/1/retweets");
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
                .post(IntegrationTestUtil.getBaseURI() + "/tweets/1/retweets");
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
                .post(IntegrationTestUtil.getBaseURI() + "/tweets/"+tweetId+"/retweets");
        domain.setResponse(response);
    }


}
