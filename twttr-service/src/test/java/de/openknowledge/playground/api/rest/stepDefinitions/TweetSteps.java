package de.openknowledge.playground.api.rest.stepDefinitions;

import com.github.database.rider.core.api.dataset.DataSetExecutor;
import com.github.database.rider.core.configuration.DataSetConfig;
import com.github.database.rider.core.connection.ConnectionHolderImpl;
import com.github.database.rider.core.dataset.DataSetExecutorImpl;
import com.github.database.rider.core.util.EntityManagerProvider;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import de.openknowledge.playground.api.rest.supportCode.SharedDomain;
import de.openknowledge.playground.api.rest.supportCode.converter.convertedClasses.LikeEntity;
import de.openknowledge.playground.api.rest.supportCode.converter.convertedClasses.TweetEntity;
import de.openknowledge.playground.api.rest.supportCode.dataSetBuilder.DBSetCreator;
import org.junit.Rule;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class TweetSteps {
    private SharedDomain domain;
    public TweetSteps (SharedDomain domain) {
        this.domain = domain;
    }

    @Rule
    EntityManagerProvider entityManagerProvider = EntityManagerProvider.instance("test-local");
    DataSetExecutor dbExecutor;
    @Before
    public void init () {
        ConnectionHolderImpl db = new ConnectionHolderImpl(entityManagerProvider.connection());
        this.dbExecutor = DataSetExecutorImpl.instance(db);
    }


    @Given("a stored tweet with id 1")
    public void a_stored_tweet_with_id() {
        TweetEntity entity = new TweetEntity(1, "Example content", new Date(System.currentTimeMillis()),0, "max");
        entity.setAuthorId(0);
        List<TweetEntity> tweets = new LinkedList<>();
        tweets.add(entity);

        DBSetCreator creator = new DBSetCreator(dbExecutor);
        creator.createTweetDataSet(new DataSetConfig(""), tweets);
    }

    @Given("a stored tweet with id 1 from user max")
    public void a_stored_tweet_with_id_from_user() {
        TweetEntity entity = new TweetEntity(1, "Example content", new Date(System.currentTimeMillis()),0, "max");
        entity.setAuthorId(0);
        List<TweetEntity> tweets = new LinkedList<>();
        tweets.add(entity);

        DBSetCreator creator = new DBSetCreator(dbExecutor);
        creator.createTweetDataSet(new DataSetConfig(""), tweets);
    }

    @Given("a stored tweet with id 1 from user max with content \"Example content\"")
    public void a_stored_tweet_with_id_from_user_with_content_Example_Content() {
        TweetEntity entity = new TweetEntity(1, "Example content", new Date(System.currentTimeMillis()),0, "max");
        entity.setAuthorId(0);
        List<TweetEntity> tweets = new LinkedList<>();
        tweets.add(entity);

        DBSetCreator creator = new DBSetCreator(dbExecutor);
        creator.createTweetDataSet(new DataSetConfig(""), tweets);
    }

    @Given("following tweets got persisted in presented order")
    public void following_tweets_got_persisted_in_presented_order(List<TweetEntity> tweets) {
        DBSetCreator creator = new DBSetCreator(dbExecutor);
        AtomicLong time = new AtomicLong(System.currentTimeMillis()-10000000);
        tweets.forEach(tweetEntity -> {
            tweetEntity.setPubDate(new Date(time.addAndGet(1000)));
            tweetEntity.setAuthorId(domain.getAccounts().get(tweetEntity.getAuthorName()).getAccountId());
        });
        creator.createTweetDataSet(new DataSetConfig(""), tweets);
    }

    @Given("following tweets got persisted from user john with id 2 in presented order")
    public void following_tweets_got_persisted_from_user_john_with_id_in_presented_order(List<TweetEntity> tweets) {
        DBSetCreator creator = new DBSetCreator(dbExecutor);
        AtomicLong time = new AtomicLong(System.currentTimeMillis()-10000000);
        tweets.forEach(tweetEntity -> {
            tweetEntity.setPubDate(new Date(time.addAndGet(1000)));
            tweetEntity.setAuthorId(2);
        });
        creator.createTweetDataSet(new DataSetConfig(""), tweets);
    }

    @Given("there is no tweet with id 9999")
    public void there_is_no_tweet_with_id() {
        List<TweetEntity> tweets = new LinkedList<>();
        DBSetCreator creator = new DBSetCreator(dbExecutor);
        creator.createTweetDataSet(new DataSetConfig(""), tweets);
    }

    @Given("a stored tweet with id 1 in status CANCELED from user max")
    public void a_stored_tweet_with_id_in_status_CANCELED_from_user() {
        TweetEntity entity = new TweetEntity(1, "Example content", new Date(System.currentTimeMillis()),1, "max");
        entity.setAuthorId(0);
        List<TweetEntity> tweets = new LinkedList<>();
        tweets.add(entity);

        DBSetCreator creator = new DBSetCreator(dbExecutor);
        creator.createTweetDataSet(new DataSetConfig(""), tweets);
    }










}
