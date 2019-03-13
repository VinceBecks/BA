package de.openknowledge.playground.api.rest.security.stepDefinition;

import com.github.database.rider.core.api.dataset.DataSetExecutor;
import com.github.database.rider.core.configuration.DataSetConfig;
import com.github.database.rider.core.connection.ConnectionHolderImpl;
import com.github.database.rider.core.dataset.DataSetExecutorImpl;
import com.github.database.rider.core.util.EntityManagerProvider;
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

    @Given("a stored tweet with id 1")
    public void a_stored_tweet_with_id() {
        dbExecutor.createDataSet(new DataSetConfig("tweet/a-stored-tweet-with-id-1-from-max.json"));
    }

    @Given("a stored tweet with id {int} from user {string}")
    public void a_stored_tweet_with_id_from_user(Integer tweetId, String userName) {
        String [] stmts = new String [2];
        stmts[0] = "DELETE FROM TAB_TWEET WHERE TWEET_ID = " + tweetId + ";";
        stmts[1] = "INSERT INTO TAB_TWEET(TWEET_ID, CONTENT, PUBLISH_DATE, STATE, AUTHOR, ROOT_TWEET) VALUES (" + tweetId + ", 'Example Bla content',CURRENT_TIMESTAMP,  0,  " + domain.getAccount(userName).getAccountId() + ", NULL );" ;
        dbExecutor.executeStatements(stmts);
    }

    @Given("a stored tweet with id 1 from user max with content \"Example Content\"")
    public void a_stored_tweet_with_id_from_user_with_content_Example_Content() {
        dbExecutor.createDataSet(new DataSetConfig("tweet/a-stored-tweet-with-id-1-from-max.json"));
    }

    @Given("there is no tweet with id {int}")
    public void there_is_no_tweet_with_id(Integer tweetId) {
        String [] stmts = new String [1];
        stmts[0] = "DELETE FROM TAB_TWEET WHERE TWEET_ID = " + tweetId + ";";
        dbExecutor.executeStatements(stmts);
    }

    @Given("a stored tweet with id {int} in status CANCELED from user {string}")
    public void a_stored_tweet_with_id_in_status_CANCELED_from_user(Integer tweetId, String userName) {
        dbExecutor.createDataSet(new DataSetConfig("tweet/a-stored-tweet-from-max-in-status-canceled.json"));
    }

    @Given("the tweet with id 1 got liked by users max and john")
    public void the_tweet_with_id_got_liked_by_users_max_and_john() {
        dbExecutor.createDataSet(new DataSetConfig("like/maxAndJohnLikeTweetWithId1.json"));
    }

    @Given("the tweet with id {int} got retweeted by users max and john")
    public void the_tweet_with_id_got_retweeted_by_users_max_and_john(Integer int1) {
        dbExecutor.createDataSet(new DataSetConfig("retweets/max-and-john-retweeted-tweet-with-id-1.json"));
    }

    @Given("the tweet with id {int} got liked by {int} user and retweeted by {int} users")
    public void the_tweet_with_id_got_liked_by_user_and_retweeted_by_users(Integer int1, Integer int2, Integer int3) {
        dbExecutor.createDataSet(new DataSetConfig("tweet/tweet-with-id-1-got-liked-1-times-and-retweeted-2-times.json"));
    }



}
