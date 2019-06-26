package de.openknowledge.playground.api.rest.stepDefinitions;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import de.openknowledge.playground.api.rest.supportCode.IntegrationTestUtil;
import de.openknowledge.playground.api.rest.supportCode.SharedDomain;
import de.openknowledge.playground.api.rest.supportCode.converter.convertedClasses.FollowerEntity;
import de.openknowledge.playground.api.rest.supportCode.dataSetBuilder.DBConnection;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.keycloak.authorization.client.AuthzClient;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.LinkedList;
import java.util.List;

public class GetFollowerSteps  {

    private SharedDomain domain;
    public GetFollowerSteps(SharedDomain domain) {
        this.domain = domain;
    }

    @Given("the user john with id 2 has two followers jane and lena")
    public void the_user_john_with_id_has_two_followers_jane_and_lena() {
        List<FollowerEntity> follower = new LinkedList<>();
        follower.add(new FollowerEntity(3, 2));
        follower.add(new FollowerEntity(6, 2));

        new DBConnection().insertFollower(follower);
    }


    @When("a client sends a GET {string} request for (user|moderator) {string} to get a list of follower from user john")
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


    @When("a client sends a GET {string} request for user {string} to get a list of follower from a moderator")
    public void a_client_sends_a_GET_request_for_user_to_get_a_list_of_follower_from_a_moderator(String additionalPath, String userName) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(userName))
                .when()
                .get(IntegrationTestUtil.getBaseURI() + additionalPath);
        domain.setResponse(response);
    }


    @When("a client sends a GET {string} request without a valid token to get a list of follower from user john")
    public void a_client_sends_a_GET_request_without_a_valid_token_to_get_a_list_of_follower_from_user_john(String additionalPath) {
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

    @When("a client sends a GET {string} request for user {string} to get a list of the specified tweet")
    public void a_client_sends_a_GET_request_for_user_to_get_a_list_of_the_specified_tweet(String additionalPath, String userName) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(userName))
                .when()
                .get(IntegrationTestUtil.getBaseURI() + additionalPath);
        domain.setResponse(response);
    }
}
