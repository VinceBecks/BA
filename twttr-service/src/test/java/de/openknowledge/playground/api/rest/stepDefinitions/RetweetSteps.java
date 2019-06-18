package de.openknowledge.playground.api.rest.stepDefinitions;

import com.github.database.rider.core.api.dataset.DataSetExecutor;
import com.github.database.rider.core.configuration.DataSetConfig;
import com.github.database.rider.core.connection.ConnectionHolderImpl;
import com.github.database.rider.core.dataset.DataSetExecutorImpl;
import com.github.database.rider.core.util.EntityManagerProvider;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import de.openknowledge.playground.api.rest.supportCode.converter.convertedClasses.TweetEntity;
import de.openknowledge.playground.api.rest.supportCode.dataSetBuilder.DBSetCreator;
import org.junit.Rule;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class RetweetSteps {

    @Rule
    EntityManagerProvider entityManagerProvider = EntityManagerProvider.instance("test-local");
    DataSetExecutor dbExecutor;
    @Before
    public void init () {
        this.dbExecutor = DataSetExecutorImpl.instance(new ConnectionHolderImpl(entityManagerProvider.connection()));
    }

    @Given("a stored retweet with id 2 from tweet with id 1")
    public void a_stored_retweet_with_id_from_tweet_with_id() {
        List<TweetEntity> tweets = new LinkedList<>();

        TweetEntity entity = new TweetEntity(1, "Example content", new Date(System.currentTimeMillis()),0, "max");
        entity.setAuthorId(0);
        tweets.add(entity);

        entity = new TweetEntity(2, "Example content", new Date(System.currentTimeMillis()),0, "max");
        entity.setAuthorId(0);
        entity.setRootTweetId(1);
        tweets.add(entity);

        DBSetCreator creator = new DBSetCreator(dbExecutor);
        creator.createTweetDataSet(new DataSetConfig(""), tweets);
    }

    @Given("the tweet with id {int} got retweeted by users max and john")
    public void the_tweet_with_id_got_retweeted_by_users_max_and_john(Integer int1) {
        List<TweetEntity> tweets = new LinkedList<>();

        TweetEntity entity = new TweetEntity(1, "Example content", new Date(System.currentTimeMillis()),0, "martha");
        entity.setAuthorId(1);
        tweets.add(entity);

        entity = new TweetEntity(2, "Example content", new Date(System.currentTimeMillis()),0, "max");
        entity.setAuthorId(0);
        entity.setRootTweetId(1);
        tweets.add(entity);

        entity = new TweetEntity(3, "Example content", new Date(System.currentTimeMillis()),0, "john");
        entity.setAuthorId(2);
        entity.setRootTweetId(1);
        tweets.add(entity);

        DBSetCreator creator = new DBSetCreator(dbExecutor);
        creator.createTweetDataSet(new DataSetConfig(""), tweets);
    }

    @Given("a stored tweet with id 1 from user john and content \"Example content\" has a retweet with id 2 from user jane")
    public void a_stored_tweet_with_id_from_user_john_and_content_has_a_retweet_with_id_from_user_jane() {
        List<TweetEntity> tweets = new LinkedList<>();

        TweetEntity entity = new TweetEntity(1, "Example content", new Date(System.currentTimeMillis()),0, "john");
        entity.setAuthorId(2);
        tweets.add(entity);

        entity = new TweetEntity(2, "Example content", new Date(System.currentTimeMillis()),0, "jane");
        entity.setAuthorId(3);
        entity.setRootTweetId(1);
        tweets.add(entity);
    }
}
