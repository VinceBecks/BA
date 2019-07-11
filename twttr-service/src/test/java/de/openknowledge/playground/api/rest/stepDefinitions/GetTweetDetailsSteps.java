package de.openknowledge.playground.api.rest.stepDefinitions;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import de.openknowledge.playground.api.rest.security.application.tweet.DetailedTweet;
import de.openknowledge.playground.api.rest.supportCode.IntegrationTestUtil;
import de.openknowledge.playground.api.rest.supportCode.SharedDomain;
import de.openknowledge.playground.api.rest.supportCode.domain.TweetEntity;
import de.openknowledge.playground.api.rest.supportCode.dataBase.DBConnection;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.Matchers;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class GetTweetDetailsSteps {

    private SharedDomain domain;
    public GetTweetDetailsSteps (SharedDomain domain) {
        this.domain = domain;
    }

    @Given("a stored tweet with id {int} from user {string} and content {string}")
    public void a_stored_tweet_with_id_from_user_and_content(Integer tweetId, String userName, String content) {
        TweetEntity entity = TweetEntity.builderInstance()
                .withTweetId(tweetId)
                .withContent(content)
                .withPubDate(new Date(System.currentTimeMillis()))
                .withState(0)
                .withAuthorId(domain.getAccount(userName).getAccountId())
                .build();
        List<TweetEntity> tweets = new LinkedList<>();
        tweets.add(entity);

        DBConnection.insertTweets(tweets);
    }



    @Given("the tweet with id {int} has a retweet with id {int} from user jane that hasn´t got liked")
    public void the_tweet_with_id_has_a_retweet_with_id_from_user_jane_that_hasnt_got_liked(Integer tweetId, Integer retweetId) {
        List<TweetEntity> tweets = new LinkedList<>();

        TweetEntity entity = TweetEntity.builderInstance()
                .withTweetId(retweetId)
                .withContent("Example content")
                .withPubDate(new Date(System.currentTimeMillis()))
                .withState(0)
                .withAuthorId(3)
                .withRootTweetId(tweetId)
                .build();
        tweets.add(entity);

        DBConnection.insertTweets(tweets);
    }


    @When("a client sends a GET {string} request for (user|moderator) {string} to get detailed information about the specified (tweet|retweet)")
    public void a_client_sends_a_GET_request_for_user_to_get_detailed_information_about_the_specified_tweet(String additionalPath, String userName) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(userName))
                .when()
                .get(IntegrationTestUtil.getBaseURI() + additionalPath);
        domain.setResponse(response);
    }



    @When("a client sends a GET {string} request without a valid token to get detailed information about the specified tweet")
    public void a_client_sends_a_GET_request_without_a_valid_token_to_get_detailed_information_about_the_specified_tweet(String additionalPath) {
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


    @Then("the HTTP response body will contain following JSON with detailed information about the tweet with id 1")
    public void the_HTTP_response_body_will_contain_following_JSON_with_detailed_information_about_the_tweet_with_id(DetailedTweet expectedTweet) {
        domain.getResponse().then()
                .contentType(MediaType.APPLICATION_JSON)
                .body("tweetId", Matchers.equalTo(expectedTweet.getTweetId()))
                .body("content", Matchers.equalTo(expectedTweet.getContent()))
                .body("author.userId", Matchers.equalTo(expectedTweet.getAuthor().getUserId()))
                .body("author.firstName", Matchers.equalTo(expectedTweet.getAuthor().getFirstName()))
                .body("author.lastName", Matchers.equalTo(expectedTweet.getAuthor().getLastName()))
                .body("numLiker", Matchers.equalTo(expectedTweet.getNumLiker()))
                .body("numRetweets", Matchers.equalTo(expectedTweet.getNumRetweets()))
                .body("rootTweet", Matchers.isEmptyOrNullString());
    }

    //fürBA: auf rootTweet.tweetId achten
    @Then("the HTTP response body will contain following JSON with detailed information about the retweet with id 2")
    public void the_HTTP_response_body_will_contain_following_JSON_with_detailed_information_about_the_retweet_with_id(DetailedTweet expectedTweet) {
        domain.getResponse().then()
                .contentType(MediaType.APPLICATION_JSON)
                .body("tweetId", Matchers.equalTo(expectedTweet.getTweetId()))
                .body("content", Matchers.equalTo(expectedTweet.getContent()))
                .body("author.userId", Matchers.equalTo(expectedTweet.getAuthor().getUserId()))
                .body("author.firstName", Matchers.equalTo(expectedTweet.getAuthor().getFirstName()))
                .body("author.lastName", Matchers.equalTo(expectedTweet.getAuthor().getLastName()))
                .body("numLiker", Matchers.equalTo(expectedTweet.getNumLiker()))
                .body("numRetweets", Matchers.equalTo(expectedTweet.getNumRetweets()))
                .body("rootTweet.tweetId", Matchers.equalTo(expectedTweet.getRootTweet().getTweetId()));
    }
}
