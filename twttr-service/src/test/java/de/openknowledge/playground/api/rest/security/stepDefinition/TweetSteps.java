package de.openknowledge.playground.api.rest.security.stepDefinition;

import com.github.database.rider.core.api.dataset.DataSetExecutor;
import com.github.database.rider.core.connection.ConnectionHolderImpl;
import com.github.database.rider.core.dataset.DataSetExecutorImpl;
import com.github.database.rider.core.util.EntityManagerProvider;
import cucumber.api.PendingException;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import de.openknowledge.playground.api.rest.security.supportCode.SharedDomain;
import org.junit.Rule;

public class TweetSteps {
    private SharedDomain domain;

    @Rule
    EntityManagerProvider entityManagerProvider = EntityManagerProvider.instance("test-local");

    DataSetExecutor dbExecutor;
    @Before
    public void init () {
        this.dbExecutor = DataSetExecutorImpl.instance(new ConnectionHolderImpl(entityManagerProvider.connection()));
    }


    public TweetSteps (SharedDomain domain) {
        this.domain = domain;
    }


    @Given("a stored tweet from {string} with id {int}")
    public void a_stored_tweet_from_with_id(String string, Integer tweetId) {
        String [] stmts = new String [2];
        stmts[0] = "DELETE FROM TAB_TWEET WHERE TWEET_ID = " + tweetId + ";";
        stmts[1] = "INSERT INTO TAB_TWEET(TWEET_ID, CONTENT, PUBLISH_DATE, STATE, AUTHOR, ROOT_TWEET) VALUES (" + tweetId + ", 'Example content',CURRENT_TIMESTAMP,  0,  0, NULL );" ;
        dbExecutor.executeStatements(stmts);
    }
}
