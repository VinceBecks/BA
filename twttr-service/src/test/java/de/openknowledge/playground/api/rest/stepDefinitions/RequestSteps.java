package de.openknowledge.playground.api.rest.stepDefinitions;

import cucumber.api.java.en.When;
import de.openknowledge.playground.api.rest.supportCode.converter.convertedClasses.NewTweet;
import de.openknowledge.playground.api.rest.supportCode.IntegrationTestUtil;
import de.openknowledge.playground.api.rest.supportCode.SharedDomain;
import de.openknowledge.playground.api.rest.supportCode.converter.convertedClasses.GetTweetsQueryParams;
import de.openknowledge.playground.api.rest.supportCode.converter.convertedClasses.GetUsersQueryParams;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.representations.idm.authorization.AuthorizationResponse;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

public class RequestSteps {

    private SharedDomain domain;

    public RequestSteps (SharedDomain domain) {
        this.domain = domain;
    }

    //Authentication Steps

    @When("a client sends a POST request to \\/auth\\/realms\\/twttr\\/protocol\\/openid-connect\\/token to get a valid token for {string}")
    public void a_client_sends_a_POST_request_to_auth_realms_twttr_protocol_openid_connect_token_to_get_a_valid_token_for (String userName) {
        AuthorizationResponse response = AuthzClient.create().authorization(userName, domain.getPasswordFromUser(userName)).authorize();
        domain.addValidToken(userName, response.getToken());
    }

    @When("a client sends a request to get a valid token for user {string} with an incorrect password")
    public void a_client_sends_a_request_to_get_a_valid_token_for_user_max_with_an_incorrect_password (String userName) {
        try {
            AuthorizationResponse response = AuthzClient.create().authorization(userName, "XXX").authorize();
            domain.addValidToken(userName, response.getToken());
        } catch (Exception e) {
            domain.addValidToken(userName, null);
        }
    }

    @When("a client sends a request to get a valid token for user {string} with an incorrect userName")
    public void a_client_sends_a_request_to_get_a_valid_token_for_user_max_with_an_incorrect_userName (String userName) {
        try {
            AuthorizationResponse response = AuthzClient.create().authorization("XXX", domain.getPasswordFromUser(userName)).authorize();
            domain.addValidToken(userName, response.getToken());
        } catch (Exception e) {
            domain.addValidToken(userName, null);
        }
    }

    @When("a client sends a request to get a valid token for user {string} without an userName")
    public void a_client_sends_a_request_to_get_a_valid_token_for_user_max_without_an_userName (String userName) {
        try {
            AuthorizationResponse response = AuthzClient.create().authorization(null, domain.getPasswordFromUser(userName)).authorize();
            domain.addValidToken(userName, response.getToken());
        } catch (Exception e) {
            domain.addValidToken(userName, null);
        }
    }

    @When("a client sends a request to get a valid token for user {string} without an password")
    public void a_client_sends_a_request_to_get_a_valid_token_for_user_without_an_password (String userName) {
        try {
            AuthorizationResponse response = AuthzClient.create().authorization(userName, null).authorize();
            domain.addValidToken(userName, response.getToken());
        } catch (Exception e) {
            domain.addValidToken(userName, null);
        }
    }



    //Cancel Tweet Steps

    @When("a client sends a DELETE {string} request for user {string}")
    public void a_client_sends_a_DELETE_request_for_user(String additionalPath, String userName) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(userName))
                .when()
                .delete(IntegrationTestUtil.getBaseURI() + additionalPath);
        domain.setResponse(response);
    }


    @When("a client sends a request for user {string} to cancel the tweet with id {int}")
    public void a_client_sends_a_request_for_user_to_cancel_the_tweet_with_id(String userName, Integer tweetId) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(userName))
                .when()
                .delete(IntegrationTestUtil.getBaseURI() + "/tweets/" + tweetId);
        domain.setResponse(response);
    }


    @When("a client sends a request for moderator {string} to cancel the tweet with id {int}")
    public void a_client_sends_a_request_for_moderator_to_cancel_the_tweet_with_id(String moderatorName, Integer tweetId) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(moderatorName))
                .when()
                .delete(IntegrationTestUtil.getBaseURI() + "/tweets/" + tweetId);
        domain.setResponse(response);
    }


    @When("a client sends a request without a valid token to cancel the tweet with id {int}")
    public void a_client_sends_a_request_without_a_valid_token_to_cancel_the_tweet_with_id(Integer tweetId) {
        String randomToken = "xxx";
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + randomToken)
                .when()
                .delete(IntegrationTestUtil.getBaseURI() + "/tweets/" + tweetId);
        domain.setResponse(response);
    }




    //Create Tweet Steps

    @When("a client sends a POST {string} request for the user {string} to create a new tweet and the body contains following JSON")
    public void sendRequestToCreateNewTweet(String additionalPath, String userName, NewTweet newTweet) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(userName))
                .body(newTweet)
                .when()
                .post(IntegrationTestUtil.getBaseURI() + additionalPath);
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
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(userName))
                .body(newTweet)
                .when()
                .post(IntegrationTestUtil.getBaseURI() + "/tweets");
        domain.setResponse(response);
    }

    @When("a client sends a request to create a new tweet without a valid token of an user")
    public void a_client_sends_a_request_to_create_a_new_tweet_without_a_valid_token_of_an_user() {
        NewTweet newTweet = new NewTweet("An example content");
        String randomToken = "xxx";

        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + randomToken)
                .body(newTweet)
                .when()
                .post(IntegrationTestUtil.getBaseURI() + "/tweets");
        domain.setResponse(response);
    }

    @When("a client sends a request to create a tweet for the moderator {string}")
    public void a_client_sends_a_request_to_create_a_tweet_for_the_moderator(String moderatorName) {
        NewTweet newTweet = new NewTweet("An example content");
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(moderatorName))
                .body(newTweet)
                .when()
                .post(IntegrationTestUtil.getBaseURI() + "/tweets");
        domain.setResponse(response);
    }




    //Follow User Steps

    @When("a client sends a POST {string} request for user {string} to follow user john")
    public void a_client_sends_a_POST_request_for_user_to_follow_user_john(String additionalPath, String followingUser) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(followingUser))
                .when()
                .post(IntegrationTestUtil.getBaseURI() + additionalPath);
        domain.setResponse(response);
    }

    @When("a client sends a request for user {string} to follow user {string}")
    public void a_client_sends_a_request_for_user_to_follow_user(String followerUser, String followedUser) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(followerUser))
                .when()
                .post(IntegrationTestUtil.getBaseURI() + "/users/" + domain.getAccount(followedUser).getAccountId() + "/follower");
        domain.setResponse(response);
    }

    @When("a client sends a request to follow an user without a valid token")
    public void a_client_sends_a_request_to_follow_an_user_without_a_valid_token() {
        String randomToken = "XXX";
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + randomToken)
                .when()
                .post(IntegrationTestUtil.getBaseURI() + "/users/1/follower");
        domain.setResponse(response);
    }


    @When("a client sends a request to follow the account of a moderator")
    public void a_client_sends_a_request_to_follow_the_account_of_a_moderator() {
        String validToken = AuthzClient.create().authorization("max", "password").authorize().getToken();
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + validToken)
                .when()
                .post(IntegrationTestUtil.getBaseURI() + "/users/4/follower");
        domain.setResponse(response);
    }


    @When("a client sends a request for the moderator {string} to follow a user")
    public void a_client_sends_a_request_for_the_moderator_to_follow_a_user(String moderatorName) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(moderatorName))
                .when()
                .post(IntegrationTestUtil.getBaseURI() + "/users/0/follower");
        domain.setResponse(response);
    }


    @When("a client sends a request to follow the user with id {int}")
    public void a_client_sends_a_request_to_follow_the_user_with_id(Integer userId) {
        String validToken = AuthzClient.create().authorization("max", "password").authorize().getToken();
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + validToken)
                .when()
                .post(IntegrationTestUtil.getBaseURI() + "/users/" + userId + "/follower");
        domain.setResponse(response);
    }




    //Get Follower Steps

    @When("a client sends a GET {string} request for user {string} to get a list of follower from user john")
    public void a_client_sends_a_GET_request_for_user_to_get_a_list_of_follower_from_user_john(String additionalPath, String userName) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(userName))
                .when()
                .get(IntegrationTestUtil.getBaseURI() + additionalPath);
        domain.setResponse(response);
    }


    @When("a client sends a request to to get a list of follower from a moderator")
    public void a_client_sends_a_request_to_to_get_a_list_of_follower_from_a_moderator() {
        String validToken = AuthzClient.create().authorization("max", "password").authorize().getToken();
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + validToken)
                .when()
                .get(IntegrationTestUtil.getBaseURI() + "/users/4/follower");
        domain.setResponse(response);
    }


    @When("a client sends a request without a valid token of an user to get a list of follower from a specified user")
    public void a_client_sends_a_request_without_a_valid_token_of_an_user_to_get_a_list_of_follower_from_a_specified_user() {
        String randomToken = "XXX";

        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + randomToken)
                .when()
                .get(IntegrationTestUtil.getBaseURI() + "/users/0/follower");
        domain.setResponse(response);
    }


    @When("a client sends a request for moderator {string} to get a list of follower from a user")
    public void a_client_sends_a_request_for_moderator_to_get_a_list_of_follower_from_a_user(String moderatorName) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(moderatorName))
                .when()
                .get(IntegrationTestUtil.getBaseURI() + "/users/0/follower");
        domain.setResponse(response);
    }

    @When("a client sends a request to get a list of follower from the user with id {int}")
    public void a_client_sends_a_request_to_get_a_list_of_follower_from_the_user_with_id(Integer userId) {
        String validToken = AuthzClient.create().authorization("max", "password").authorize().getToken();
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + validToken)
                .when()
                .get(IntegrationTestUtil.getBaseURI() + "/users/" + userId + "/follower");
        domain.setResponse(response);
    }


    //Get Liker Steps

    @When("a client sends a GET {string} request for user {string} to get a list of likers of the tweet with id {int}")
    public void a_client_sends_a_GET_request_for_user_to_get_a_list_of_likers_of_the_tweet_with_id(String additionalPath, String userName, Integer tweetId) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(userName))
                .when()
                .get(IntegrationTestUtil.getBaseURI() + additionalPath);
        domain.setResponse(response);
    }

    @When("a client sends a request without a valid token of an user to get a list of liker of a specified tweet")
    public void a_client_sends_a_request_without_a_valid_token_of_an_user_to_get_a_list_of_liker_of_a_specified_tweet() {
        String randomToken = "XXX";

        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + randomToken)
                .when()
                .get(IntegrationTestUtil.getBaseURI() + "/tweets/1/liker");
        domain.setResponse(response);
    }

    @When("a client sends a request for moderator {string} to get a list of liker of a tweet")
    public void a_client_sends_a_request_for_moderator_to_get_a_list_of_liker_of_a_tweet(String moderatorName) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(moderatorName))
                .when()
                .get(IntegrationTestUtil.getBaseURI() + "/tweets/0/liker");
        domain.setResponse(response);
    }

    @When("a client sends a request to get a list of liker of the tweet with id {int}")
    public void a_client_sends_a_request_to_get_a_list_of_liker_of_the_tweet_with_id(Integer tweetId) {
        String validToken = AuthzClient.create().authorization("max", "password").authorize().getToken();
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + validToken)
                .when()
                .get(IntegrationTestUtil.getBaseURI() + "/tweets/" + tweetId + "/liker");
        domain.setResponse(response);
    }

    @When("a client sends a request to get a list of liker from the tweet with id {int}")
    public void a_client_sends_a_request_to_get_a_list_of_liker_from_the_tweet_with_id(Integer tweetId) {
        String validToken = AuthzClient.create().authorization("max", "password").authorize().getToken();

        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + validToken)
                .when()
                .get(IntegrationTestUtil.getBaseURI() + "/tweets/"+tweetId+"/retweets/authors");
        domain.setResponse(response);
    }


    //Get Retweeter Steps

    @When("a client sends a GET {string} request for user {string} to get a list of retweeter of the tweet with id 1")
    public void a_client_sends_a_GET_request_for_user_to_get_a_list_of_retweeter_of_the_tweet_with_id(String additionalPath, String userName) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(userName))
                .when()
                .get(IntegrationTestUtil.getBaseURI() + additionalPath);
        domain.setResponse(response);
    }


    @When("a client sends a request without a valid token of an user to get a list of retweeter of a tweet")
    public void a_client_sends_a_request_without_a_valid_token_of_an_user_to_get_a_list_of_retweeter_of_a_tweet() {
        String randomToken = "XXX";

        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + randomToken)
                .when()
                .get(IntegrationTestUtil.getBaseURI() + "/tweets/1/retweets/authors");
        domain.setResponse(response);
    }

    @When("a client sends a request for moderator {string} to get a list of retweeter of a tweet")
    public void a_client_sends_a_request_for_moderator_to_get_a_list_of_retweeter_of_a_tweet(String moderatorName) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(moderatorName))
                .when()
                .get(IntegrationTestUtil.getBaseURI() + "/tweets/1/retweets/authors");
        domain.setResponse(response);
    }

    @When("a client sends a request to get a list of retweeter of the tweet with id {int}")
    public void a_client_sends_a_request_to_get_a_list_of_retweeter_of_the_tweet_with_id(Integer tweetId) {
        String validToken = AuthzClient.create().authorization("max", "password").authorize().getToken();

        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + validToken)
                .when()
                .get(IntegrationTestUtil.getBaseURI() + "/tweets/"+tweetId+"/retweets/authors");
        domain.setResponse(response);
    }


    @When("a client sends a request to get a list of retweeter from the tweet with id {int}")
    public void a_client_sends_a_request_to_get_a_list_of_retweeter_from_the_tweet_with_id(Integer tweetId) {
        String validToken = AuthzClient.create().authorization("max", "password").authorize().getToken();

        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + validToken)
                .when()
                .get(IntegrationTestUtil.getBaseURI() + "/tweets/"+tweetId+"/retweets/authors");
        domain.setResponse(response);
    }


    //Get Tweet details Steps

    @When("a client sends a GET {string} request for (user|moderator) {string} to get detailed information about the tweet with id {int}")
    public void a_client_sends_a_GET_request_for_user_to_get_detailed_information_about_the_tweet_with_id(String additionalPath, String userName, Integer tweetId) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(userName))
                .when()
                .get(IntegrationTestUtil.getBaseURI() + additionalPath);
        domain.setResponse(response);

    }

    @When("a client sends a GET {string} request for user {string} to get detailed information about the retweet with id 2")
    public void a_client_sends_a_GET_request_for_user_to_get_detailed_information_about_the_retweet_with_id(String additionalPath, String userName) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(userName))
                .when()
                .get(IntegrationTestUtil.getBaseURI() + additionalPath);
        domain.setResponse(response);
    }


    @When("a client sends a request without a valid token to get detailed information about a tweet")
    public void a_client_sends_a_request_without_a_valid_token_to_get_detailed_information_about_a_tweet() {
        String randomToken = "XXX";

        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + randomToken)
                .when()
                .get(IntegrationTestUtil.getBaseURI() + "/tweets/1");
        domain.setResponse(response);
    }


    @When("a client sends a request to get detailed information about the tweet with id {int}")
    public void a_client_sends_a_request_to_get_detailed_information_about_the_tweet_with_id(Integer tweetId) {
        String validToken = AuthzClient.create().authorization("max", "password").authorize().getToken();

        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + validToken)
                .when()
                .get(IntegrationTestUtil.getBaseURI() + "/tweets/" + tweetId);
        domain.setResponse(response);
    }




    //Get Tweet from User Steps

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


    @When("a client sends a request to get a list of tweets from user {string} with following Query Params")
    public void a_client_sends_a_request_for_user_to_get_a_list_of_tweets_from_user_with_following_Query_Params(String userToGetTweetsFrom, GetTweetsQueryParams params) {
        String validToken = AuthzClient.create().authorization("max", "password").authorize().getToken();
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + validToken)
                .when()
                .get(IntegrationTestUtil.getBaseURI() + "/users/" + domain.getAccount(userToGetTweetsFrom).getAccountId() + "/tweets" + params.getQueryString());
        domain.setResponse(response);
    }


    @When("a client sends a request without a valid token to get tweets from a specified user")
    public void a_client_sends_a_request_without_a_valid_token_to_get_tweets_from_a_specified_user() {
        String randomToken = "XXX";

        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + randomToken)
                .when()
                .get(IntegrationTestUtil.getBaseURI() + "/users/2/tweets");
        domain.setResponse(response);
    }




    //Get Tweets Steps

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


    @When("a client sends a request without a valid token to get tweets")
    public void a_client_sends_a_request_without_a_valid_token_to_get_tweets() {
        String randomToken = "XXX";

        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + randomToken)
                .when()
                .get(IntegrationTestUtil.getBaseURI() + "/tweets");
        domain.setResponse(response);
    }





    //Get Users Steps

    @When("a client sends a {string} {string} request for user {string} to get a list of users")
    public void a_client_sends_a_request_for_user_to_get_a_list_of_users(String httpMethod, String additionalPath, String userName) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(userName))
                .when()
                .get(IntegrationTestUtil.getBaseURI() + additionalPath);
        domain.setResponse(response);
    }

    @When("a client sends a {string} {string} request for moderator {string} to get a list of users")
    public void a_client_sends_a_request_for_moderator_to_get_a_list_of_users(String httpMethod, String additionalPath, String moderatorName) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(moderatorName))
                .when()
                .get(IntegrationTestUtil.getBaseURI() + additionalPath);
        domain.setResponse(response);
    }


    @When("a client sends a request to get a list of users with following QueryParams")
    public void a_client_sends_a_request_to_get_a_list_of_users_with_following_QueryParams(GetUsersQueryParams params) {
        String validToken = AuthzClient.create().authorization("max", "password").authorize().getToken();

        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + validToken)
                .when()
                .get(IntegrationTestUtil.getBaseURI() + "/users" + params.getQueryString());
        domain.setResponse(response);
    }

    @When("a client sends a request without a valid token to get a list of users")
    public void a_client_sends_a_request_without_a_valid_token_to_get_a_list_of_users() {
        String randomToken = "XXX";

        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + randomToken)
                .when()
                .get(IntegrationTestUtil.getBaseURI() + "/users");
        domain.setResponse(response);
    }



    //Like a Tweet Steps

    @When("a client sends a POST {string} request for user {string} to like the tweet with id 1")
    public void a_client_sends_a_POST_request_for_user_to_like_the_tweet_with_id(String additionalPath, String userName) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(userName))
                .when()
                .post(IntegrationTestUtil.getBaseURI() + additionalPath);
        domain.setResponse(response);
    }


    @When("a client sends a request without a valid token of an user to like a specified tweet")
    public void a_client_sends_a_request_without_a_valid_token_of_an_user_to_like_a_specified_tweet() {
        String randomToken = "XXX";

        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + randomToken)
                .when()
                .post(IntegrationTestUtil.getBaseURI() + "/tweets/1/liker");
        domain.setResponse(response);
    }

    @When("a client sends a request for moderator {string} to like a specified tweet")
    public void a_client_sends_a_request_for_moderator_to_like_a_specified_tweet(String moderatorName) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(moderatorName))
                .when()
                .post(IntegrationTestUtil.getBaseURI() + "/tweets/1/liker");
        domain.setResponse(response);
    }

    @When("a client sends a request to like the tweet with id {int}")
    public void a_client_sends_a_request_to_like_the_tweet_with_id(Integer tweetId) {
        String validToken = AuthzClient.create().authorization("max", "password").authorize().getToken();
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + validToken)
                .when()
                .post(IntegrationTestUtil.getBaseURI() + "/tweets/"+tweetId+"/liker");
        domain.setResponse(response);

    }




    //Retweet Tweet steps

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




    // Unfollow User Steps

    @When("a client sends a DELETE {string} request for user {string} to unfollow user john")
    public void a_client_sends_a_DELETE_request_for_user_to_unfollow_user_john(String additionalPath, String userName) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(userName))
                .when()
                .delete(IntegrationTestUtil.getBaseURI() + additionalPath);
        domain.setResponse(response);
    }


    @When("a client sends a request to unfollow a specified user without a valid token of an user")
    public void a_client_sends_a_request_to_unfollow_a_specified_user_without_a_valid_token_of_an_user() {
        String randomToken = "XXX";

        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + randomToken)
                .when()
                .delete(IntegrationTestUtil.getBaseURI() + "/users/1/follower");
        domain.setResponse(response);
    }

    @When("a client sends a request for moderator {string} to unfollow a specified user")
    public void a_client_sends_a_request_for_moderator_to_unfollow_a_specified_user(String moderatorName) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(moderatorName))
                .when()
                .delete(IntegrationTestUtil.getBaseURI() + "/users/1/follower");
        domain.setResponse(response);
    }

    @When("a client sends a request to unfollow the account of a moderator")
    public void a_client_sends_a_request_to_unfollow_the_account_of_a_moderator() {
        String validToken = AuthzClient.create().authorization("max", "password").authorize().getToken();
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + validToken)
                .when()
                .delete(IntegrationTestUtil.getBaseURI() + "/users/4/follower");
        domain.setResponse(response);
    }

    @When("a client sends a request to unfollow the user with id {int}")
    public void a_client_sends_a_request_to_unfollow_the_user_with_id(Integer userId) {
        String validToken = AuthzClient.create().authorization("max", "password").authorize().getToken();
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + validToken)
                .when()
                .delete(IntegrationTestUtil.getBaseURI() + "/users/"+userId+"/follower");
        domain.setResponse(response);
    }





    //Unlike Tweet Steps

    @When("a client sends a DELETE {string} request for user {string} to unlike the tweet with id 1")
    public void a_client_sends_a_DELETE_request_for_user_to_unlike_the_tweet_with_id(String additionalPath, String userName) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(userName))
                .when()
                .delete(IntegrationTestUtil.getBaseURI() + additionalPath);
        domain.setResponse(response);
    }

    @When("a client sends a request without a valid token of an user to unlike a specified tweet")
    public void a_client_sends_a_request_without_a_valid_token_of_an_user_to_unlike_a_specified_tweet() {
        String randomToken = "XXX";

        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + randomToken)
                .when()
                .delete(IntegrationTestUtil.getBaseURI() + "/tweets/1/liker");
        domain.setResponse(response);
    }

    @When("a client sends a request for moderator {string} to unlike a specified tweet")
    public void a_client_sends_a_request_for_moderator_to_unlike_a_specified_tweet(String moderatorName) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(moderatorName))
                .when()
                .delete(IntegrationTestUtil.getBaseURI() + "/tweets/1/liker");
        domain.setResponse(response);
    }

    @When("a client sends a request to unlike the tweet with id {int}")
    public void a_client_sends_a_request_to_unlike_the_tweet_with_id(Integer tweetId) {
        String validToken = AuthzClient.create().authorization("max", "password").authorize().getToken();
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + validToken)
                .when()
                .delete(IntegrationTestUtil.getBaseURI() + "/tweets/"+tweetId+"/liker");
        domain.setResponse(response);
    }

}
