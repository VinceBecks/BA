package de.openknowledge.playground.api.rest.security.stepDefinition;

import cucumber.api.PendingException;
import cucumber.api.java.en.When;
import de.openknowledge.playground.api.rest.security.supportCode.SharedDomain;
import de.openknowledge.playground.api.rest.security.supportCode.converter.convertedClasses.GetUsersQueryParams;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.keycloak.authorization.client.AuthzClient;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

public class GetUsersSteps {

    private SharedDomain domain;

    public GetUsersSteps (SharedDomain domain) {
        this.domain = domain;
    }

    @When("a client sends a {string} {string} request for user {string} to get a list of users")
    public void a_client_sends_a_request_for_user_to_get_a_list_of_users(String httpMethod, String additionalPath, String userName) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(userName))
                .when()
                .get(domain.basePath() + additionalPath);
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
                .get(domain.basePath() + additionalPath);
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
                .get(domain.basePath() + "/users" + params.getQueryString());
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
                .get(domain.basePath() + "/users");
        domain.setResponse(response);
    }


}
