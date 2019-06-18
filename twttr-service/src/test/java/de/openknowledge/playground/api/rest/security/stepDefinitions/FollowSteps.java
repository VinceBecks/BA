package de.openknowledge.playground.api.rest.security.stepDefinitions;

import com.github.database.rider.core.api.dataset.DataSetExecutor;
import com.github.database.rider.core.configuration.DataSetConfig;
import com.github.database.rider.core.connection.ConnectionHolderImpl;
import com.github.database.rider.core.dataset.DataSetExecutorImpl;
import com.github.database.rider.core.util.EntityManagerProvider;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import de.openknowledge.playground.api.rest.security.supportCode.SharedDomain;
import de.openknowledge.playground.api.rest.security.supportCode.converter.convertedClasses.FollowerEntity;
import de.openknowledge.playground.api.rest.security.supportCode.dataSetBuilder.DBSetCreator;
import org.junit.Rule;

import java.util.LinkedList;
import java.util.List;

public class FollowSteps {

    @Rule
    EntityManagerProvider entityManagerProvider = EntityManagerProvider.instance("test-local");
    DataSetExecutor dbExecutor;
    @Before
    public void init () {
        this.dbExecutor = DataSetExecutorImpl.instance(new ConnectionHolderImpl(entityManagerProvider.connection()));
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

    @Given("the user max is a follower of user john with id 2")
    public void the_user_max_is_a_follower_of_user_john_with_id() {
        List<FollowerEntity> follower = new LinkedList<>();
        follower.add(new FollowerEntity(0,2));
        DBSetCreator creator = new DBSetCreator(dbExecutor);
        creator.createFollowerDataSet(new DataSetConfig(""), follower);
    }

    @Given("the user max is not a follower of user john with id 2")
    public void the_user_max_is_not_a_follower_of_user_john_with_id() {
        List<FollowerEntity> follower = new LinkedList<>();
        DBSetCreator creator = new DBSetCreator(dbExecutor);
        creator.createFollowerDataSet(new DataSetConfig(""), follower);
    }


    @Given("the user john with id 2 has two followers jane and lena")
    public void the_user_john_with_id_has_two_followers_jane_and_lena() {
        List<FollowerEntity> follower = new LinkedList<>();
        follower.add(new FollowerEntity(3, 2));
        follower.add(new FollowerEntity(6, 2));

        DBSetCreator creator = new DBSetCreator(dbExecutor);
        creator.createFollowerDataSet(new DataSetConfig(""), follower);
    }
}
