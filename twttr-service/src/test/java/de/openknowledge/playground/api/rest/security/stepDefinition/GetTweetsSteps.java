package de.openknowledge.playground.api.rest.security.stepDefinition;

import cucumber.api.java.en.When;
import de.openknowledge.playground.api.rest.security.supportCode.IntegrationTestUtil;
import de.openknowledge.playground.api.rest.security.supportCode.SharedDomain;
import de.openknowledge.playground.api.rest.security.supportCode.converter.convertedClasses.GetTweetsQueryParams;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

public class GetTweetsSteps {
    private SharedDomain domain;

    public GetTweetsSteps (SharedDomain domain) {
        this.domain = domain;
    }

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

}
