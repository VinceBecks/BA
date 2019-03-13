package de.openknowledge.playground.api.rest.security.stepDefinition;

import com.github.database.rider.core.api.dataset.DataSetExecutor;
import com.github.database.rider.core.configuration.DataSetConfig;
import com.github.database.rider.core.connection.ConnectionHolderImpl;
import com.github.database.rider.core.dataset.DataSetExecutorImpl;
import com.github.database.rider.core.util.EntityManagerProvider;
import cucumber.api.PendingException;
import cucumber.api.java.Before;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import de.openknowledge.playground.api.rest.security.supportCode.SharedDomain;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.DataSetException;
import org.junit.Rule;
import org.keycloak.authorization.client.AuthzClient;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

public class FollowUserSteps {
    private SharedDomain domain;

    @Rule
    EntityManagerProvider entityManagerProvider = EntityManagerProvider.instance("test-local");

    DataSetExecutor dbExecutor;

    @Before
    public void init () {
        this.dbExecutor = DataSetExecutorImpl.instance(new ConnectionHolderImpl(entityManagerProvider.connection()));
    }

    public FollowUserSteps (SharedDomain domain) {
        this.domain = domain;
    }

    @When("a client sends a {string} {string} request for user {string} to follow user {string}")
    public void a_client_sends_a_request_for_user_to_follow_user(String httpMethod, String additionalPath, String followingUser, String followedUser) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(followingUser))
                .when()
                .post(domain.basePath() + additionalPath);
        domain.setResponse(response);
    }

    @When("a client sends a request for user {string} to follow user {string}")
    public void a_client_sends_a_request_for_user_to_follow_user(String followerUser, String followedUser) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(followerUser))
                .when()
                .post(domain.basePath() + "/users/" + domain.getAccount(followedUser).getAccountId() + "/follower");
        domain.setResponse(response);
    }

    @When("a client sends a request to follow a user without a valid token")
    public void a_client_sends_a_request_to_follow_a_user_without_a_valid_token() {
        String randomToken = "XXX";
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + randomToken)
                .when()
                .post(domain.basePath() + "/users/1/follower");
        domain.setResponse(response);
    }


    @When("a client sends a request to follow the account of a moderator")
    public void a_client_sends_a_request_to_follow_the_account_of_a_moderator() {
        String validToken = AuthzClient.create().authorization("max", "password").authorize().getToken();
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + validToken)
                .when()
                .post(domain.basePath() + "/users/4/follower");
        domain.setResponse(response);
    }


    @When("a client sends a request for the moderator {string} to follow a user")
    public void a_client_sends_a_request_for_the_moderator_to_follow_a_user(String moderatorName) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(moderatorName))
                .when()
                .post(domain.basePath() + "/users/0/follower");
        domain.setResponse(response);
    }


    @When("a client sends a request to follow the user with id {int}")
    public void a_client_sends_a_request_to_follow_the_user_with_id(Integer userId) {
        String validToken = AuthzClient.create().authorization("max", "password").authorize().getToken();
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + validToken)
                .when()
                .post(domain.basePath() + "/users/" + userId + "/follower");
        domain.setResponse(response);
    }

    @Then("the user max will be a follower of user john")
    public void the_user_max_will_be_a_follower_of_user_john() {
        try {
            dbExecutor.compareCurrentDataSetWith(new DataSetConfig("follower/maxFollowsJohn.json"), new String [0]);
        } catch (DatabaseUnitException e) {
            //todo: Gibt bestimmt eine bessere Exception zu werfen
            throw new PendingException( );
        }
    }
}
