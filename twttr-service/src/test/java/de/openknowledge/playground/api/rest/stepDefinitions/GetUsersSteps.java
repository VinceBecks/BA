package de.openknowledge.playground.api.rest.stepDefinitions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import de.openknowledge.playground.api.rest.supportCode.IntegrationTestUtil;
import de.openknowledge.playground.api.rest.supportCode.SharedDomain;
import de.openknowledge.playground.api.rest.supportCode.dataBase.DBConnection;
import de.openknowledge.playground.api.rest.supportCode.domain.AccountEntity;
import de.openknowledge.playground.api.rest.supportCode.domain.GetUsersQueryParams;
import de.openknowledge.playground.api.rest.supportCode.domain.UserDTO;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.dbunit.DatabaseUnitException;
import org.hamcrest.Matchers;
import org.keycloak.authorization.client.AuthzClient;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class GetUsersSteps {

    private SharedDomain domain;
    public GetUsersSteps (SharedDomain domain) {
        this.domain = domain;
    }

    @Given("the system has persisted users")
    public void the_system_has_persisted_users(List<AccountEntity> accounts) {
        try {
            new DBConnection().updateAccounts(accounts);
        } catch (DatabaseUnitException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Given("there is no user with id {int}")
    public void there_is_no_user_with_id(int accountId) {
        //todo: Löscht Account, wenn vorhanden ... keine Prüfung, ob es ein User ist
        new DBConnection().deleteAccountIfPresent(accountId);
    }

    @When("a client sends a GET {string} request for (user|moderator) {string} to get a list of users")
    public void a_client_sends_a_GET_request_for_user_to_get_a_list_of_users(String additionalPath, String userName) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(userName))
                .when()
                .get(IntegrationTestUtil.getBaseURI() + additionalPath);
        domain.setResponse(response);
    }




    @When("a client sends a GET {string} request for user {string} to get a list of users with following QueryParameter")
    public void a_client_sends_a_GET_request_for_user_to_get_a_list_of_users_with_following_QueryParameter(String additionalPath, String userName, GetUsersQueryParams params) {
        String validToken = AuthzClient.create().authorization("max", "password").authorize().getToken();

        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + validToken)
                .when()
                .get(IntegrationTestUtil.getBaseURI() + additionalPath + params.getQueryString());
        domain.setResponse(response);
    }


    @When("a client sends a GET {string} request without a valid token to get a list of users with following QueryParameter")
    public void a_client_sends_a_GET_request_without_a_valid_token_to_get_a_list_of_users_with_following_QueryParameter(String additionalPath) {
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
    public void the_returned_users_will_be_the_users_with_ids_in_presented_order(List<Integer> expectedIds) {
        domain.getResponse().then()
                .contentType(MediaType.APPLICATION_JSON)
                .body("size()", Matchers.equalTo(expectedIds.size()));

        for (int i=0; i<expectedIds.size(); i++) {
            domain.getResponse().then()
                    .body("["+i+"].userId", Matchers.equalTo(expectedIds.get(i)));
        }
    }
}
