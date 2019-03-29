package de.openknowledge.playground.api.rest.security.stepDefinition;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.core.api.dataset.DataSetExecutor;
import com.github.database.rider.core.configuration.DataSetConfig;
import com.github.database.rider.core.connection.ConnectionHolderImpl;
import com.github.database.rider.core.dataset.DataSetExecutorImpl;
import com.github.database.rider.core.util.EntityManagerProvider;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import de.openknowledge.playground.api.rest.security.supportCode.SharedDomain;
import de.openknowledge.playground.api.rest.security.supportCode.AccountDataSet;
import de.openknowledge.playground.api.rest.security.supportCode.converter.convertedClasses.Account;
import org.junit.Rule;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.representations.idm.authorization.AuthorizationResponse;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class AccountSteps {

    private SharedDomain domain;
    static boolean first = true;

    @Rule
    EntityManagerProvider entityManagerProvider = EntityManagerProvider.instance("test-local");

    DataSetExecutor dbExecutor;
    @Before
    public void init () {
        this.dbExecutor = DataSetExecutorImpl.instance(new ConnectionHolderImpl(entityManagerProvider.connection()));
    }

    public AccountSteps (SharedDomain domain) {
        this.domain = domain;
    }

    @Given("the (user|moderator) {string} is authenticated")
    public void the_user_is_authenticated(String userName) {
        AuthorizationResponse response = AuthzClient.create().authorization(userName, domain.getAccount(userName).getPassword()).authorize();
        domain.addValidToken(userName, response.getToken());
    }

    @Given("the system has persisted users")
    public void the_system_has_persisted_users(List<Account> accounts) {
        try {
            AccountDataSet accountDataSet = new AccountDataSet(accounts);
            String json = new ObjectMapper().writeValueAsString(accountDataSet);
            Writer writer = new FileWriter ("src/test/resources/datasets/users.json");
            writer.write(json);
            writer.close();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        dbExecutor.createDataSet(new DataSetConfig("datasets/users.json"));


    }

    @Given("there is no user with id 9999")
    public void there_is_no_user_with_id() {
        dbExecutor.createDataSet(new DataSetConfig("users.json"));
    }

    @Given("the user max isn´t a follower of user john with id 2")
    public void the_user_isn_t_a_follower_of_user_with_id() {
        dbExecutor.createDataSet(new DataSetConfig("follower/empty-follower-list.json"));
    }

    @Given("the user max is already a follower of user john")
    public void the_user_max_is_already_a_follower_of_user_john() {
        dbExecutor.createDataSet(new DataSetConfig("follower/max-follows-john.json"));
    }

    @Given("user max follows the users john and jane")
    public void user_max_follows_the_users_john_and_jane() {
        dbExecutor.createDataSet(new DataSetConfig("follower/max-follows-john-and-jane.json"));
    }

    @Given("test")
    public void test() {
        // Write code here that turns the phrase above into concrete actions
    }
}
