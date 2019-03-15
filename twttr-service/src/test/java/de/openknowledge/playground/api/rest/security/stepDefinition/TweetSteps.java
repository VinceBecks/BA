package de.openknowledge.playground.api.rest.security.stepDefinition;

import com.github.database.rider.core.api.dataset.DataSetExecutor;
import com.github.database.rider.core.configuration.DataSetConfig;
import com.github.database.rider.core.connection.ConnectionHolderImpl;
import com.github.database.rider.core.dataset.DataSetExecutorImpl;
import com.github.database.rider.core.util.EntityManagerProvider;
import cucumber.api.PendingException;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import de.openknowledge.playground.api.rest.security.domain.tweet.TweetDTO;
import de.openknowledge.playground.api.rest.security.supportCode.SharedDomain;
import io.cucumber.datatable.DataTable;
import org.junit.Rule;

import java.util.List;

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

    @Given("a stored tweet with id 1 from user max")
    public void a_stored_tweet_with_id_from_user() {
        dbExecutor.createDataSet(new DataSetConfig("tweet/a-stored-tweet-with-id-1-from-max.json"));
    }

    @Given("a stored tweet with id 1 from user max with content \"Example Content\"")
    public void a_stored_tweet_with_id_from_user_with_content_Example_Content() {
        dbExecutor.createDataSet(new DataSetConfig("tweet/a-stored-tweet-with-id-1-from-max.json"));
    }

    @Given("following tweets got persisted in presented order")
    public void following_tweets_got_persisted_in_presented_order(DataTable dataTable) {
        //todo: was mit übergebenen Parameter machen? Wird nicht gebraucht...
        dbExecutor.createDataSet(new DataSetConfig("tweet/list-of-tweets.json"));
    }

    @Given("following tweets got persisted from user john with id 2 in presented order")
    public void following_tweets_got_persisted_from_user_john_with_id_in_presented_order(DataTable dataTable) {
        //todo: Was mit übergebenen Parameter machen? ... wird nicht benötigt
        dbExecutor.createDataSet(new DataSetConfig("tweet/list-of-tweets-from-john.json"));
    }

    @Given("there is no tweet with id {int}")
    public void there_is_no_tweet_with_id(Integer tweetId) {
        String [] stmts = new String [1];
        stmts[0] = "DELETE FROM TAB_TWEET WHERE TWEET_ID = " + tweetId + ";";
        dbExecutor.executeStatements(stmts);
    }

    @Given("a stored tweet with id 1 in status CANCELED from user max")
    public void a_stored_tweet_with_id_in_status_CANCELED_from_user() {
        dbExecutor.createDataSet(new DataSetConfig("tweet/a-stored-tweet-with-id-1-from-max-in-status-canceled.json"));
    }

    @Given("the tweet with id 1 got liked by users max and john")
    public void the_tweet_with_id_got_liked_by_users_max_and_john() {
        dbExecutor.createDataSet(new DataSetConfig("like/max-and-john-like-tweet-with-id-1.json"));
    }

    @Given("the user max is a liker of tweet with id 1")
    public void the_user_max_is_a_liker_of_tweet_with_id() {
        dbExecutor.createDataSet(new DataSetConfig("like/max-is-liker-of-tweet-with-id-1.json"));
    }

    @Given("the user max is not a liker of the tweet with id 1")
    public void the_user_max_is_not_a_liker_of_tweet_with_id() {
        dbExecutor.createDataSet(new DataSetConfig("like/empty-liker-list.json"));
    }

    @Given("the tweet with id {int} got retweeted by users max and john")
    public void the_tweet_with_id_got_retweeted_by_users_max_and_john(Integer int1) {
        dbExecutor.createDataSet(new DataSetConfig("retweets/max-and-john-retweeted-tweet-with-id-1.json"));
    }

    @Given("the tweet with id 1 got liked by 1 user and retweeted by 2 users")
    public void the_tweet_with_id_got_liked_by_user_and_retweeted_by_users() {
        dbExecutor.createDataSet(new DataSetConfig("tweet/tweet-with-id-1-got-liked-1-times-and-retweeted-2-times.json"));
    }

    @Given("a stored retweet with id 2 from tweet with id 1")
    public void a_stored_retweet_with_id_from_tweet_with_id() {
        dbExecutor.createDataSet(new DataSetConfig("retweets/retweet-with-id-2-from-tweet-with-id-1.json"));
    }

    @Given("the user max is a follower of user john with id {int}")
    public void the_user_max_is_a_follower_of_user_john_with_id(Integer int1) {
        dbExecutor.createDataSet(new DataSetConfig("follower/max-follows-john.json"));
    }

    @Given("the user max is not a follower of user john with id {int}")
    public void the_user_max_is_not_a_follower_of_user_john_with_id(Integer int1) {
        dbExecutor.createDataSet(new DataSetConfig("follower/empty-follower-list.json"));
    }
}
