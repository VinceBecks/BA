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

public class GetLikerSteps  {

    private SharedDomain domain;
    public GetLikerSteps(SharedDomain domain) { this.domain = domain; }

    @Given("the tweet with id 1 got liked by users max and john")
    public void the_tweet_with_id_got_liked_by_users_max_and_john() {
        List<LikeEntity> likes = new LinkedList<>();
        likes.add(new LikeEntity(1,0));
        likes.add(new LikeEntity(1,2));
        new DBConnection().insertLikes(likes);
    }

    @When("a client sends a GET {string} request for (user|moderator) {string} to get a list of likers of the specified tweet")
    public void a_client_sends_a_GET_request_for_user_to_get_a_list_of_likers_of_the_tweet_with_id(String additionalPath, String userName) {
        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + domain.tokenFromAccount(userName))
                .when()
                .get(IntegrationTestUtil.getBaseURI() + additionalPath);
        domain.setResponse(response);
    }

    @When("a client sends a GET {string} request without a valid token to get a list of likers of the specified tweet")
    public void a_client_sends_a_GET_request_without_a_valid_token_to_get_a_list_of_likers_of_the_specified_tweet(String additionalPath) {
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

}
