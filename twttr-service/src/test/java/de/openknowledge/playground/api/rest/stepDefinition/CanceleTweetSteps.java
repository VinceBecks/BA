package de.openknowledge.playground.api.rest.stepDefinition;

import com.github.database.rider.core.api.dataset.DataSetExecutor;
import com.github.database.rider.core.connection.ConnectionHolderImpl;
import com.github.database.rider.core.dataset.DataSetExecutorImpl;
import com.github.database.rider.core.util.EntityManagerProvider;
import cucumber.api.PendingException;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import de.openknowledge.playground.api.rest.supportCode.IntegrationTestUtil;
import de.openknowledge.playground.api.rest.supportCode.SharedDomain;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Rule;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

public class CanceleTweetSteps {

    private SharedDomain domain;

    @Rule
    EntityManagerProvider entityManagerProvider = EntityManagerProvider.instance("test-local");

    DataSetExecutor dbExecutor;
    @Before
    public void init () {
        this.dbExecutor = DataSetExecutorImpl.instance(new ConnectionHolderImpl(entityManagerProvider.connection()));
    }

    public CanceleTweetSteps (SharedDomain domain) {
        this.domain = domain;
    }



    @When("a client sends a DELETE {string} request for user {string}")
    public void a_client_sends_a_DELETE_request_for_user(String additionalPath, String userName) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(userName))
                .when()
                .delete(IntegrationTestUtil.getBaseURI() + additionalPath);
        domain.setResponse(response);
    }


    @When("a client sends a request for user {string} to cancel the tweet with id {int}")
    public void a_client_sends_a_request_for_user_to_cancel_the_tweet_with_id(String userName, Integer tweetId) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(userName))
                .when()
                .delete(IntegrationTestUtil.getBaseURI() + "/tweets/" + tweetId);
        domain.setResponse(response);
    }


    @When("a client sends a request for moderator {string} to cancel the tweet with id {int}")
    public void a_client_sends_a_request_for_moderator_to_cancel_the_tweet_with_id(String moderatorName, Integer tweetId) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(moderatorName))
                .when()
                .delete(IntegrationTestUtil.getBaseURI() + "/tweets/" + tweetId);
        domain.setResponse(response);
    }


    @When("a client sends a request without a valid token to cancel the tweet with id {int}")
    public void a_client_sends_a_request_without_a_valid_token_to_cancel_the_tweet_with_id(Integer tweetId) {
        String randomToken = "xxx";
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + randomToken)
                .when()
                .delete(IntegrationTestUtil.getBaseURI() + "/tweets/" + tweetId);
        domain.setResponse(response);
    }


}