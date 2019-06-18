package de.openknowledge.playground.api.rest.stepDefinitions;

import com.github.database.rider.core.api.dataset.DataSetExecutor;
import com.github.database.rider.core.configuration.DataSetConfig;
import com.github.database.rider.core.connection.ConnectionHolderImpl;
import com.github.database.rider.core.dataset.DataSetExecutorImpl;
import com.github.database.rider.core.util.EntityManagerProvider;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import de.openknowledge.playground.api.rest.supportCode.converter.convertedClasses.LikeEntity;
import de.openknowledge.playground.api.rest.supportCode.converter.convertedClasses.TweetEntity;
import de.openknowledge.playground.api.rest.supportCode.dataSetBuilder.DBSetCreator;
import org.junit.Rule;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class LikeSteps {

    @Rule
    EntityManagerProvider entityManagerProvider = EntityManagerProvider.instance("test-local");
    DataSetExecutor dbExecutor;
    @Before
    public void init () {
        this.dbExecutor = DataSetExecutorImpl.instance(new ConnectionHolderImpl(entityManagerProvider.connection()));
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

    @Given("the retweet hasn´t got liked")
    public void the_retweet_hasn_t_got_liked() {
        List<LikeEntity> likes = new LinkedList<>();
        DBSetCreator creator = new DBSetCreator(dbExecutor);
        creator.createLikesDataSet(new DataSetConfig(""), likes);
    }
}
