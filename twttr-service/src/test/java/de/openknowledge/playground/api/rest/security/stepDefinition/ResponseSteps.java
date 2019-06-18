package de.openknowledge.playground.api.rest.security.stepDefinition;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import cucumber.api.PendingException;
import cucumber.api.java.en.Then;
import de.openknowledge.playground.api.rest.security.application.tweet.DetailedTweet;
import de.openknowledge.playground.api.rest.security.domain.tweet.TweetDTO;
import de.openknowledge.playground.api.rest.security.supportCode.SharedDomain;
import de.openknowledge.playground.api.rest.security.supportCode.converter.convertedClasses.ErrorMessage;
import de.openknowledge.playground.api.rest.security.domain.accounts.UserDTO;
import de.openknowledge.playground.api.rest.security.supportCode.converter.convertedClasses.IntegerList;
import io.restassured.response.Response;
import org.hamcrest.Matchers;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class ResponseSteps {
    private SharedDomain domain;

    public ResponseSteps (SharedDomain domain) {
        this.domain = domain;
    }


    @Then("the HTTP response body contains following JSON of an error message:")
    public void the_HTTP_response_body_contains_following_JSON_of_an_error_message(ErrorMessage expectedErrorMessage) {
        domain.getResponse().then()
                .contentType(MediaType.APPLICATION_JSON)
                .body("errorMessage", Matchers.equalTo(expectedErrorMessage.getErrorMessage()));
    }

    @Then("the HTTP response body contains following JSON of a list of error messages:")
    public void the_HTTP_response_body_contains_following_JSON_of_a_list_of_error_messages(String expectedJson) {
        try {
            List<ErrorMessage> errors = new ObjectMapper().readValue(expectedJson, new TypeReference<List<ErrorMessage>>() {});
            domain.getResponse().then()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("size()",Matchers.equalTo(1))
                    .body("[0].errorMessage", Matchers.equalTo(errors.get(0).getErrorMessage()));
        } catch (IOException e) {
            throw new PendingException();
        }
    }

    @Then("the HTTP response status-code will be {int}")
    public void the_HTTP_response_status_code_will_be(Integer expectedStatusCode) {
        Response response = domain.getResponse();
        response.then()
                .statusCode(expectedStatusCode);
    }

    @Then("the HTTP response body contains following JSON of a new Tweet, while the tweetId and the publish-date got generated by the system")
    public void checkCreatedTweetResponse(TweetDTO expectedTweet) {
        domain.getResponse().then()
                .contentType(MediaType.APPLICATION_JSON)
                .body("tweetId", Matchers.notNullValue(Integer.class))
                .body("content", Matchers.equalTo(expectedTweet.getContent()))
                .body("pubDate", Matchers.notNullValue(Date.class))
                .body("author.userId", Matchers.equalTo(expectedTweet.getAuthor().getUserId()))
                .body("author.firstName", Matchers.equalTo(expectedTweet.getAuthor().getFirstName()))
                .body("author.lastName", Matchers.equalTo(expectedTweet.getAuthor().getLastName()))
                .body("author.role", Matchers.equalTo(expectedTweet.getAuthor().getRole().toString()))
                .body("rootTweet", Matchers.isEmptyOrNullString());
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

    @Then("the HTTP response body contains following JSON with a list of users")
    public void the_HTTP_response_body_contains_following_JSON_with_a_list_of_users(String expectedJson) {
        List<UserDTO> expectedUsers;
        try {
            expectedUsers = new ObjectMapper().readValue(expectedJson, new TypeReference<List<UserDTO>>(){});
        } catch (IOException e) {
            throw new PendingException();
        }

        domain.getResponse().then()
                .contentType(MediaType.APPLICATION_JSON)
                .body("size()", Matchers.equalTo(expectedUsers.size()));

        for (int i=0; i<expectedUsers.size(); i++) {
            domain.getResponse().then()
                    .body("["+i+"].userId", Matchers.equalTo(expectedUsers.get(i).getUserId()))
                    .body("["+i+"].firstName", Matchers.equalTo(expectedUsers.get(i).getFirstName()))
                    .body("["+i+"].lastName", Matchers.equalTo(expectedUsers.get(i).getLastName()))
                    .body("["+i+"].role", Matchers.equalTo(expectedUsers.get(i).getRole().toString()));
        }
    }

    @Then("the returned users will be the users with ids {Ids} in presented order")
    public void the_returned_users_will_be_the_users_with_ids_in_presented_order(IntegerList ids) {
        List<Integer> expectedIds = ids.getIntegerList();

        domain.getResponse().then()
                .contentType(MediaType.APPLICATION_JSON)
                .body("size()", Matchers.equalTo(expectedIds.size()));

        for (int i=0; i<expectedIds.size(); i++) {
            domain.getResponse().then()
                    .body("["+i+"].userId", Matchers.equalTo(expectedIds.get(i)));
        }
    }
}
