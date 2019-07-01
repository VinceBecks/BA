package de.openknowledge.playground.api.rest.supportCode.dataBase;

import de.openknowledge.playground.api.rest.supportCode.domain.AccountEntity;
import de.openknowledge.playground.api.rest.supportCode.domain.FollowerEntity;
import de.openknowledge.playground.api.rest.supportCode.domain.LikeEntity;
import de.openknowledge.playground.api.rest.supportCode.domain.TweetEntity;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.builder.DataRowBuilder;
import org.dbunit.dataset.builder.DataSetBuilder;

import java.util.List;


public class CustomizedDataSetBuilder {

    public static IDataSet tweetDataSet(List<TweetEntity> tweets) {
        DataSetBuilder builder = null;
        try {
            builder = new DataSetBuilder();
        } catch (DataSetException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

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

            try {
                rowBuilder.add();
            } catch (DataSetException e) {
                e.printStackTrace();
                throw new RuntimeException();
            }
        }

        try {
            return builder.build();
        } catch (DataSetException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public static IDataSet likeDataSet(List<LikeEntity> likes) {
        DataSetBuilder builder = null;
        try {
            builder = new DataSetBuilder();
        } catch (DataSetException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

        for (LikeEntity entity : likes) {
            DataRowBuilder rowBuilder = builder.newRow("TAB_LIKER")
                    .with("USER_ID", entity.getUserId())
                    .with("TWEET_ID", entity.getTweetId());
            try {
                rowBuilder.add();
            } catch (DataSetException e) {
                e.printStackTrace();
                throw new RuntimeException();
            }
        }

        try {
            return builder.build();
        } catch (DataSetException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public static IDataSet followerDataSet(List<FollowerEntity> follower) {
        DataSetBuilder builder = null;
        try {
            builder = new DataSetBuilder();
        } catch (DataSetException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

        for (FollowerEntity entity : follower) {
            DataRowBuilder rowBuilder = builder.newRow("TAB_FOLLOWER")
                    .with("FOLLOWER_ID", entity.getFollowerId())
                    .with("FOLLOWING_ID", entity.getFollowingId());
            try {
                rowBuilder.add();
            } catch (DataSetException e) {
                e.printStackTrace();
                throw new RuntimeException();
            }
        }

        try {
            return builder.build();
        } catch (DataSetException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

    }

    public static IDataSet accountDataSet (List<AccountEntity> accounts) {
        DataSetBuilder builder = null;
        try {
            builder = new DataSetBuilder();
        } catch (DataSetException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

        for (AccountEntity entity : accounts) {
            DataRowBuilder rowBuilder = builder.newRow("TAB_ACCOUNT")
                    .with("ACCOUNT_TYPE", entity.getAccountType())
                    .with("ACCOUNT_ID", entity.getAccountId())
                    .with("FIRST_NAME", entity.getFirstName())
                    .with("LAST_NAME", entity.getLastName())
                    .with("ROLE", entity.getRole())
                    .with("USERNAME", entity.getUserName());
            try {
                rowBuilder.add();
            } catch (DataSetException e) {
                e.printStackTrace();
                throw new RuntimeException();
            }
        }

        try {
            return builder.build();
        } catch (DataSetException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public static IDataSet deleteAccountFromDataSet(int accountId, IDataSet dataSet) {
        ITable table = null;
        try {
            table = dataSet.getTable("TAB_ACCOUNT");
        } catch (DataSetException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        if (table != null){
            DataSetBuilder builder = null ;
            try {
                builder = new DataSetBuilder();
                int numRows = table.getRowCount();
                for (int i=0; i<numRows; i++) {
                    Integer id = (Integer) table.getValue(i, "ACCOUNT_ID");
                    if (!id.equals(accountId)){
                        DataRowBuilder rowBuilder = builder.newRow("TAB_ACCOUNT")
                                .with("ACCOUNT_TYPE", table.getValue(i, "ACCOUNT_TYPE"))
                                .with("ACCOUNT_ID", id)
                                .with("FIRST_NAME",table.getValue(i, "FIRST_NAME"))
                                .with("LAST_NAME", table.getValue(i, "LAST_NAME"))
                                .with("ROLE", table.getValue(i, "ROLE"))
                                .with("USERNAME", table.getValue(i, "USERNAME"));

                        rowBuilder.add();
                    }
                }
                return builder.build();
            } catch (DataSetException e) {
                e.printStackTrace();
                throw new RuntimeException();
            }
        }else {
            throw  new RuntimeException();
        }
    }

    public static IDataSet deleteTweetFromDataSet(int tweetId, IDataSet dataSet) {
        ITable table = null;

        try {
            table = dataSet.getTable("TAB_TWEET");
        } catch (DataSetException e) {
            e.printStackTrace();
        }

        if (table != null){
            DataSetBuilder builder = null;
            try {
                builder = new DataSetBuilder();
                int numRows = table.getRowCount();
                for (int i=0; i<numRows; i++) {
                    Integer id = (Integer) table.getValue(i, "TWEET_ID");
                    if (!id.equals(tweetId)){
                        DataRowBuilder rowBuilder = builder.newRow("TAB_TWEET")
                                .with("TWEET_ID", table.getValue(i, "TWEET_ID"))
                                .with("ACCOUNT_ID", id)
                                .with("CONTENT",table.getValue(i, "CONTENT"))
                                .with("PUBLISH_DATE", table.getValue(i, "PUBLISH_DATE"))
                                .with("AUTHOR", table.getValue(i, "AUTHOR"))
                                .with("ROOT_TWEET", table.getValue(i, "ROOT_TWEET"));

                        rowBuilder.add();
                    }
                }
                return builder.build();
            } catch (DataSetException e) {
                e.printStackTrace();
                throw new RuntimeException();
            }
        }else {
            throw  new RuntimeException();
        }
    }


    public static IDataSet emptyTableDataSet () {
        DataSetBuilder builder = null;
        try {
             builder = new DataSetBuilder();
             builder.ensureTableIsPresent("TAB_TWEET");
             builder.ensureTableIsPresent("TAB_FOLLOWER");
             builder.ensureTableIsPresent("TAB_LIKER");
             return builder.build();
        } catch (DataSetException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

}
