package de.openknowledge.playground.api.rest.security.stepDefinition;

import com.github.database.rider.core.api.dataset.DataSetExecutor;
import com.github.database.rider.core.configuration.DataSetConfig;
import com.github.database.rider.core.connection.ConnectionHolderImpl;
import com.github.database.rider.core.dataset.DataSetExecutorImpl;
import com.github.database.rider.core.util.EntityManagerProvider;
import cucumber.api.PendingException;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import de.openknowledge.playground.api.rest.security.supportCode.SharedDomain;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Rule;
import org.keycloak.authorization.client.AuthzClient;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

public class GetFollowerSteps {

    private SharedDomain domain;

    @Rule
    EntityManagerProvider entityManagerProvider = EntityManagerProvider.instance("test-local");

    DataSetExecutor dbExecutor;
    @Before
    public void init () {
        this.dbExecutor = DataSetExecutorImpl.instance(new ConnectionHolderImpl(entityManagerProvider.connection()));
    }

    public GetFollowerSteps(SharedDomain domain) {
        this.domain = domain;
    }

    @Given("the user john with id 2 has two followers jane and lena")
    public void the_user_john_with_id_has_two_followers_jane_and_lena() {
        dbExecutor.createDataSet(new DataSetConfig("follower/janeAndLenaFollowsJohn.json"));
    }


    @When("a client sends a GET {string} request for user {string} to get a list of follower from user john")
    public void a_client_sends_a_GET_request_for_user_to_get_a_list_of_follower_from_user_john(String additionalPath, String userName) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(userName))
                .when()
                .get(domain.basePath() + additionalPath);
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
                .get(domain.basePath() + "/users/4/follower");
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
                .get(domain.basePath() + "/users/0/follower");
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
                .get(domain.basePath() + "/users/0/follower");
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
                .get(domain.basePath() + "/users/" + userId + "/follower");
        domain.setResponse(response);
    }
}
