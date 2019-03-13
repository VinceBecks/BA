package de.openknowledge.playground.api.rest.security.stepDefinition;

import cucumber.api.java.en.When;
import de.openknowledge.playground.api.rest.security.supportCode.SharedDomain;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.keycloak.authorization.client.AuthzClient;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

public class GetTweetDetailsSteps {
    private SharedDomain domain;

    public GetTweetDetailsSteps (SharedDomain domain) {
        this.domain = domain;
    }


    @When("a client sends a {string} {string} request for user {string} to get detailed information about the tweet with id {int}")
    public void a_client_sends_a_request_for_user_to_get_detailed_information_about_the_tweet_with_id(String httpMethod, String additionalPath, String userName, Integer tweetId) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(userName))
                .when()
                .get(domain.basePath() + additionalPath);
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
                .get(domain.basePath() + "/tweets/1");
        domain.setResponse(response);
    }

    @When("a client sends a request to get detailed information about a tweet of the tweet with id {int}")
    public void a_client_sends_a_request_to_get_detailed_information_about_a_tweet_of_the_tweet_with_id(Integer tweetId) {
        String validToken = AuthzClient.create().authorization("max", "password").authorize().getToken();

        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + validToken)
                .when()
                .get(domain.basePath() + "/tweets/" + tweetId);
        domain.setResponse(response);
    }


    //todo: ist exact die gleich ausführung wie obere methode ... Pattern kann bearbeitet werden, sodass methode für beide Steps matched
    @When("a client sends a request to get detailed information about a tweet from  the tweet with id {int}")
    public void a_client_sends_a_request_to_get_detailed_information_about_a_tweet_from_the_tweet_with_id(Integer tweetId) {
        String validToken = AuthzClient.create().authorization("max", "password").authorize().getToken();

        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + validToken)
                .when()
                .get(domain.basePath() + "/tweets/" + tweetId);
        domain.setResponse(response);
    }
}
