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
import de.openknowledge.playground.api.rest.supportCode.converter.convertedClasses.LikeEntity;
import de.openknowledge.playground.api.rest.supportCode.converter.convertedClasses.TweetEntity;
import org.junit.Rule;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

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

    @Given("the tweet with id 1 got liked by users max and john")
    public void the_tweet_with_id_got_liked_by_users_max_and_john() {
        List<LikeEntity> likes = new LinkedList<>();
        likes.add(new LikeEntity(1,0));
        likes.add(new LikeEntity(1,2));
        DBSetCreator creator = new DBSetCreator(dbExecutor);
        creator.createLikesDataSet(new DataSetConfig(""), likes);
    }

    @Given("the user max is a liker of tweet with id 1")
    public void the_user_max_is_a_liker_of_tweet_with_id() {
        List<LikeEntity> likes = new LinkedList<>();
        likes.add(new LikeEntity(1,0));
        DBSetCreator creator = new DBSetCreator(dbExecutor);
        creator.createLikesDataSet(new DataSetConfig(""), likes);
    }

    @Given("the user max is not a liker of the tweet with id 1")
    public void the_user_max_is_not_a_liker_of_tweet_with_id() {
        List<LikeEntity> likes = new LinkedList<>();
        DBSetCreator creator = new DBSetCreator(dbExecutor);
        creator.createLikesDataSet(new DataSetConfig(""), likes);
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

    @Given("the tweet with id 1 has one liker and two retweets")
    public void the_tweet_with_id_1_has_one_liker_and_two_retweets() {
        List<TweetEntity> tweets = new LinkedList<>();

        TweetEntity entity = new TweetEntity(1, "Example content", new Date(System.currentTimeMillis()),0, "max");
        entity.setAuthorId(0);
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

        List<LikeEntity> likes = new LinkedList<>();
        likes.add(new LikeEntity(1, 0));
        creator.createLikesDataSet(new DataSetConfig(""), likes);
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

    @Given("the retweet hasn´t got liked")
    public void the_retweet_hasn_t_got_liked() {
        List<LikeEntity> likes = new LinkedList<>();
        DBSetCreator creator = new DBSetCreator(dbExecutor);
        creator.createLikesDataSet(new DataSetConfig(""), likes);
    }




}
