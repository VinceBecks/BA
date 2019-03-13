package de.openknowledge.playground.api.rest.security.stepDefinition;

import cucumber.api.java.en.When;
import de.openknowledge.playground.api.rest.security.supportCode.SharedDomain;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

public class GetTweetsSteps {
    private SharedDomain domain;

    public GetTweetsSteps (SharedDomain domain) {
        this.domain = domain;
    }

    @When("a client sends a {string} {string} request for {string} to get a list of tweets from users he follows")
    public void a_client_sends_a_request_for_to_get_a_list_of_tweets_from_users_he_follows(String httpMethod, String additionalPath, String userName) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(userName))
                .when()
                .get(domain.basePath() + additionalPath);
        domain.setResponse(response);
    }

}
