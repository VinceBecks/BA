package de.openknowledge.playground.api.rest.security.stepDefinition;

import com.github.database.rider.core.api.dataset.DataSetExecutor;
import com.github.database.rider.core.configuration.DataSetConfig;
import com.github.database.rider.core.connection.ConnectionHolderImpl;
import com.github.database.rider.core.dataset.DataSetExecutorImpl;
import com.github.database.rider.core.util.EntityManagerProvider;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import de.openknowledge.playground.api.rest.security.supportCode.dataSetBuilder.DBSetCreator;
import de.openknowledge.playground.api.rest.security.supportCode.SharedDomain;
import de.openknowledge.playground.api.rest.security.supportCode.converter.convertedClasses.FollowerEntity;
import de.openknowledge.playground.api.rest.security.supportCode.converter.convertedClasses.AccountEntity;
import org.junit.Rule;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.representations.idm.authorization.AuthorizationResponse;

import java.util.LinkedList;
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
    public void the_system_has_persisted_users(List<AccountEntity> accounts) {
        DBSetCreator creator = new DBSetCreator(dbExecutor);
        creator.createAccountDataSet(new DataSetConfig(""), accounts);
    }

    @Given("there is no user with id 9999")
    public void there_is_no_user_with_id() {
        DBSetCreator creator = new DBSetCreator(dbExecutor);
        creator.createAccountDataSet(new DataSetConfig(""), new LinkedList<AccountEntity>());
    }

    @Given("the user max isn´t a follower of user john with id 2")
    public void the_user_isn_t_a_follower_of_user_with_id() {
        List<FollowerEntity> follower = new LinkedList<>();
        DBSetCreator creator = new DBSetCreator(dbExecutor);
        creator.createFollowerDataSet(new DataSetConfig(""), follower);
    }

    @Given("the user max is already a follower of user john")
    public void the_user_max_is_already_a_follower_of_user_john() {
        List<FollowerEntity> follower = new LinkedList<>();
        follower.add(new FollowerEntity(0, 2));
        DBSetCreator creator = new DBSetCreator(dbExecutor);
        creator.createFollowerDataSet(new DataSetConfig(""), follower);
    }

    @Given("user max follows the users john and jane")
    public void user_max_follows_the_users_john_and_jane() {
        List<FollowerEntity> follower = new LinkedList<>();
        follower.add(new FollowerEntity(0, 2));
        follower.add(new FollowerEntity(0, 3));
        DBSetCreator creator = new DBSetCreator(dbExecutor);
        creator.createFollowerDataSet(new DataSetConfig(""), follower);
    }
}
