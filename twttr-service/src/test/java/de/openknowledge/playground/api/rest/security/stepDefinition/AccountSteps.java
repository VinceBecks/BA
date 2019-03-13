package de.openknowledge.playground.api.rest.security.stepDefinition;

import com.github.database.rider.core.api.dataset.DataSetExecutor;
import com.github.database.rider.core.configuration.DataSetConfig;
import com.github.database.rider.core.connection.ConnectionHolderImpl;
import com.github.database.rider.core.dataset.DataSetExecutorImpl;
import com.github.database.rider.core.util.EntityManagerProvider;
import cucumber.api.PendingException;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import de.openknowledge.playground.api.rest.security.supportCode.SharedDomain;
import org.junit.Rule;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.representations.idm.authorization.AuthorizationResponse;

public class AccountSteps {

    private SharedDomain domain;

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

    @Given("the user {string} is authenticated")
    public void the_user_is_authenticated(String userName) {
        AuthorizationResponse response = AuthzClient.create().authorization(userName, domain.getAccount(userName).getPassword()).authorize();
        domain.addValidToken(userName, response.getToken());
    }

    @Given("there is no user with id {int}")
    public void there_is_no_user_with_id(Integer userId) {
        String [] stmts = {"DELETE FROM TAB_ACCOUNTS WHERE ACCOUNT_ID = " + userId + ";"};
        dbExecutor.executeStatements(stmts);
    }

    @Given("the moderator {string} is authenticated")
    public void the_moderator_is_authenticated(String moderatorName) {
        AuthorizationResponse response = AuthzClient.create().authorization(moderatorName, domain.getAccount(moderatorName).getPassword()).authorize();
        domain.addValidToken(moderatorName, response.getToken());
    }



    @Given("the user max isn´t a follower of user john with id 2")
    public void the_user_isn_t_a_follower_of_user_with_id() {
        dbExecutor.createDataSet(new DataSetConfig("follower/emptyFollowerTable.json"));
    }

    @Given("the user max is already a follower of user john")
    public void the_user_max_is_already_a_follower_of_user_john() {
        dbExecutor.createDataSet(new DataSetConfig("follower/maxFollowsJohn.json"));
    }








}
