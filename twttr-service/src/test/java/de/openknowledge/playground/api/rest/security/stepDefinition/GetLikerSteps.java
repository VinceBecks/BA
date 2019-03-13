package de.openknowledge.playground.api.rest.security.stepDefinition;

import cucumber.api.java.en.When;
import de.openknowledge.playground.api.rest.security.supportCode.SharedDomain;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.keycloak.authorization.client.AuthzClient;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

public class GetLikerSteps {

    private SharedDomain domain;
    public GetLikerSteps(SharedDomain domain) {
        this.domain = domain;
    }


    @When("a client sends a {string} {string} request for user {string} to get a list of likers of the tweet with id {int}")
    public void a_client_sends_a_request_for_user_to_get_a_list_of_likers_of_the_tweet_with_id(String httpMethod, String additionalPath, String userName, Integer tweetId) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(userName))
                .when()
                .get(domain.basePath() + additionalPath);
        domain.setResponse(response);
    }

    @When("a client sends a request without a valid token to get a list of liker of a tweet")
    public void a_client_sends_a_request_without_a_valid_token_to_get_a_list_of_liker_of_a_tweet() {
        String randomToken = "XXX";

        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + randomToken)
                .when()
                .get(domain.basePath() + "/tweets/1/liker");
        domain.setResponse(response);    }

    @When("a client sends a request for moderator {string} to get a list of liker of a tweet")
    public void a_client_sends_a_request_for_moderator_to_get_a_list_of_liker_of_a_tweet(String moderatorName) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(moderatorName))
                .when()
                .get(domain.basePath() + "/tweets/0/liker");
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
                .get(domain.basePath() + "/tweets/" + tweetId + "/liker");
        domain.setResponse(response);
    }

    @When("a client sends a request to get a list of liker from  the tweet with id {int}")
    public void a_client_sends_a_request_to_get_a_list_of_liker_from_the_tweet_with_id(Integer tweetId) {
        String validToken = AuthzClient.create().authorization("max", "password").authorize().getToken();

        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + validToken)
                .when()
                .get(domain.basePath() + "/tweets/"+tweetId+"/retweets/authors");
        domain.setResponse(response);
    }


}
