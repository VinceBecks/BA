package de.openknowledge.playground.api.rest.stepDefinition;

import com.github.database.rider.core.api.dataset.DataSetExecutor;
import com.github.database.rider.core.configuration.DataSetConfig;
import com.github.database.rider.core.connection.ConnectionHolderImpl;
import com.github.database.rider.core.dataset.DataSetExecutorImpl;
import com.github.database.rider.core.util.EntityManagerProvider;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import de.openknowledge.playground.api.rest.supportCode.dataSetBuilder.DBSetCreator;
import de.openknowledge.playground.api.rest.supportCode.SharedDomain;
import de.openknowledge.playground.api.rest.supportCode.converter.convertedClasses.FollowerEntity;
import de.openknowledge.playground.api.rest.supportCode.converter.convertedClasses.AccountEntity;
import io.cucumber.datatable.DataTable;
import org.junit.Rule;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.representations.idm.authorization.AuthorizationResponse;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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


    @Given("following moderator")
    public void following_moderator (DataTable dataTable) {
        Map<String, String> account = dataTable.transpose().asMap(String.class, String.class);
        domain.setPasswordForUser(account.get("userName"), null);
        domain.setPasswordForUser(account.get("userName"), account.get("password"));
    }

    @Given("following user")
    public void following_user (DataTable dataTable) {
        Map<String, String> account = dataTable.transpose().asMap(String.class, String.class);
        domain.setPasswordForUser(account.get("userName"), account.get("password"));
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


}
