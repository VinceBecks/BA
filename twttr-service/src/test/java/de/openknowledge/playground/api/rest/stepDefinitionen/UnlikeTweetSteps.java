package de.openknowledge.playground.api.rest.stepDefinitionen;

import com.github.database.rider.core.configuration.DataSetConfig;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import de.openknowledge.playground.api.rest.supportCode.IntegrationTestUtil;
import de.openknowledge.playground.api.rest.supportCode.SharedDomain;
import de.openknowledge.playground.api.rest.supportCode.converter.convertedClasses.LikeEntity;
import de.openknowledge.playground.api.rest.supportCode.dataSetBuilder.DBConnection;
import de.openknowledge.playground.api.rest.supportCode.dataSetBuilder.DBSetCreator;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.keycloak.authorization.client.AuthzClient;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.LinkedList;
import java.util.List;

public class UnlikeTweetSteps {

    private SharedDomain domain;
    public UnlikeTweetSteps(SharedDomain domain) {
        this.domain = domain;
    }

    @When("a client sends a DELETE {string} request for user {string} to unlike the tweet with id 1")
    public void a_client_sends_a_DELETE_request_for_user_to_unlike_the_tweet_with_id(String additionalPath, String userName) {

    }

    @When("a client sends a DELETE {string} request for (user|moderator) {string} to unlike the specified tweet")
    public void a_client_sends_a_DELETE_request_for_user_to_unlike_the_specified_tweet(String additionalPath, String userName) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(userName))
                .when()
                .delete(IntegrationTestUtil.getBaseURI() + additionalPath);
        domain.setResponse(response);
    }

    @When("a client sends a DELETE {string} request without a valid token to unlike the specified tweet")
    public void a_client_sends_a_DELETE_request_without_a_valid_token_to_unlike_the_specified_tweet(String additionalPath) {
        String randomToken = "XXX";

        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + randomToken)
                .when()
                .delete(IntegrationTestUtil.getBaseURI() + additionalPath);
        domain.setResponse(response);
    }

}
