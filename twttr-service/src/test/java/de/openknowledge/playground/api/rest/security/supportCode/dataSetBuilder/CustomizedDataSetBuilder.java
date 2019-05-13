package de.openknowledge.playground.api.rest.security.supportCode.dataSetBuilder;

import de.openknowledge.playground.api.rest.security.supportCode.converter.convertedClasses.AccountEntity;
import de.openknowledge.playground.api.rest.security.supportCode.converter.convertedClasses.FollowerEntity;
import de.openknowledge.playground.api.rest.security.supportCode.converter.convertedClasses.LikeEntity;
import de.openknowledge.playground.api.rest.security.supportCode.converter.convertedClasses.TweetEntity;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.builder.DataRowBuilder;
import org.dbunit.dataset.builder.DataSetBuilder;

import java.util.List;

public class CustomizedDataSetBuilder {

    public IDataSet createTweets (List<TweetEntity> tweets) {
        DataSetBuilder builder =null;

        try {
            builder = new DataSetBuilder();
            builder.newRow("TAB_FOLLOWER").add();
            builder.newRow("TAB_LIKER").add();
            for (TweetEntity entity : tweets) {
                DataRowBuilder rowBuilder = builder.newRow("TAB_TWEET")
                        .with("TWEET_ID", entity.getTweetId())
                        .with("CONTENT", entity.getConent())
                        .with("PUBLISH_DATE", entity.getPubDate())
                        .with("STATE", entity.getState())
                        .with("AUTHOR", entity.getAuthorId());

                if (entity.getRootTweetId() != null) {
                    rowBuilder = rowBuilder.with("ROOT_TWEET", entity.getRootTweetId());
                }

                rowBuilder.add();
            }

            if (tweets.size() == 0) {       //case empty Tweet Table
                builder.newRow("TAB_TWEET").add();
            }
            return builder.build();
        } catch (DataSetException e) {
            e.printStackTrace();
        }
        return null;
    }


    public IDataSet createLikes (List<LikeEntity> likes) {
        DataSetBuilder builder =null;

        try {
            builder = new DataSetBuilder();
            for (LikeEntity entity : likes) {
                builder.newRow("TAB_LIKER")
                        .with("USER_ID", entity.getUserId())
                        .with("TWEET_ID", entity.getTweetId())
                        .add();
            }

            if (likes.size() == 0) {       //case empty Liker Table
                builder.newRow("TAB_LIKER").add();
            }
            return builder.build();
        } catch (DataSetException e) {
            e.printStackTrace();
        }
        return null;
    }



    public IDataSet createFollower (List<FollowerEntity> follower) {
        DataSetBuilder builder =null;
        try {
            builder = new DataSetBuilder();
            for (FollowerEntity entity : follower) {
                DataRowBuilder rowBuilder = builder.newRow("TAB_FOLLOWER")
                        .with("FOLLOWER_ID", entity.getFollowerId())
                        .with("FOLLOWING_ID", entity.getFollowingId());
                rowBuilder.add();
            }

            if (follower.size() == 0) {       //case empty Tweet Table
                builder.newRow("TAB_FOLLOWER").add();
            }
            return builder.build();
        } catch (DataSetException e) {
            e.printStackTrace();
        }
        return null;
    }


    public IDataSet createAccounts (List<AccountEntity> accounts) {
        DataSetBuilder builder =null;

        try {
            builder = new DataSetBuilder();
            builder.newRow("TAB_FOLLOWER").add();
            builder.newRow("TAB_LIKER").add();
            builder.newRow("TAB_TWEET").add();
            for (AccountEntity entity : accounts) {
                DataRowBuilder rowBuilder = builder.newRow("TAB_ACCOUNT")
                        .with("ACCOUNT_TYPE", entity.getAccountType())
                        .with("ACCOUNT_ID", entity.getAccountId())
                        .with("FIRST_NAME", entity.getFirstName())
                        .with("LAST_NAME", entity.getLastName())
                        .with("ROLE", entity.getRole())
                        .with("USERNAME", entity.getUserName());
                rowBuilder.add();
            }

            if (accounts.size() == 0) {       //case empty Tweet Table
                builder.newRow("TAB_FOLLOWER").add();
                builder.newRow("TAB_LIKER").add();
                builder.newRow("TAB_TWEET").add();
                builder.newRow("TAB_FOLLOWER").add();
            }
            return builder.build();
        } catch (DataSetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
