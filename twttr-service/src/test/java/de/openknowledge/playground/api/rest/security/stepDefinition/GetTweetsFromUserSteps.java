package de.openknowledge.playground.api.rest.security.stepDefinition;

import cucumber.api.java.en.When;
import de.openknowledge.playground.api.rest.security.supportCode.IntegrationTestUtil;
import de.openknowledge.playground.api.rest.security.supportCode.SharedDomain;
import de.openknowledge.playground.api.rest.security.supportCode.converter.convertedClasses.GetTweetsQueryParams;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.keycloak.authorization.client.AuthzClient;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

public class GetTweetsFromUserSteps {
    private SharedDomain domain;

    public GetTweetsFromUserSteps (SharedDomain domain) {
        this.domain = domain;
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
}