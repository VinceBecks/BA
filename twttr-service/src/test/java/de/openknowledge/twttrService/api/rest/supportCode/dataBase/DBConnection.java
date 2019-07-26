package de.openknowledge.twttrService.api.rest.supportCode.dataBase;

import com.github.database.rider.core.util.EntityManagerProvider;
import de.openknowledge.twttrService.api.rest.supportCode.domain.AccountEntity;
import de.openknowledge.twttrService.api.rest.supportCode.domain.FollowerEntity;
import de.openknowledge.twttrService.api.rest.supportCode.domain.LikeEntity;
import de.openknowledge.twttrService.api.rest.supportCode.domain.TweetEntity;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.operation.DatabaseOperation;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;


public class DBConnection {
    private final static Properties dbConfig;
    private static IDatabaseConnection connection;
    private static EntityManagerFactory emFactory;
    private static EntityManager entityManager;

    public DBConnection() {

    }

    static{
        emFactory = Persistence.createEntityManagerFactory("test-local");
        entityManager = emFactory.createEntityManager();

        dbConfig = new Properties();
        dbConfig.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, retrieveDbUnitDataTypeFactoryName());
        dbConfig.setProperty(DatabaseConfig.FEATURE_QUALIFIED_TABLE_NAMES, "false");

        try {
            connection = getConnection();
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Can´t get connection to DB");
        }
    }

    private static String retrieveDbUnitDataTypeFactoryName() {
        Map<String, Object> entityManagerProperties = emFactory.getProperties();
        String databaseDriverClazz = (String)entityManagerProperties.get("javax.persistence.jdbc.driver");
        return DbUnitDatatypeFactory.getDatatypeFactory(databaseDriverClazz);
    }

    private static IDatabaseConnection getConnection() throws DatabaseUnitException {
        connection = new DatabaseConnection(EntityManagerProvider.instance("test-local").connection());
        connection.getConfig().setPropertiesByString(dbConfig);

        return connection;
    }

    public static IDataSet getActualDataSet() {
        IDataSet actualDataSet;
        String[] tableNames = new String []{"TAB_TWEET", "TAB_ACCOUNT", "TAB_FOLLOWER", "TAB_LIKE"};
        try {
            actualDataSet = connection.createDataSet(tableNames);
        } catch (SQLException | DataSetException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        return actualDataSet;
    }


    public static void insertTweets(List<TweetEntity> tweets) {
        try {
            DatabaseOperation.INSERT.execute(connection, CustomizedDataSetBuilder.tweetDataSet(tweets));
        } catch (DatabaseUnitException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public static void insertLikes (List<LikeEntity> likes) {
        try {
            DatabaseOperation.INSERT.execute(connection, CustomizedDataSetBuilder.likeDataSet(likes));
        } catch (DatabaseUnitException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public static void insertFollower (List<FollowerEntity> follower) {
        try {
            DatabaseOperation.INSERT.execute(connection, CustomizedDataSetBuilder.followerDataSet(follower));
        } catch (DatabaseUnitException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public static void updateAccounts (List<AccountEntity> accounts) throws DatabaseUnitException, SQLException {
        DatabaseOperation.CLEAN_INSERT.execute(connection, CustomizedDataSetBuilder.accountDataSet(accounts));
    }


    public static boolean isAccountPresent(int accountId) {
        IDataSet dataSet = getActualDataSet();
        try {
            ITable table = dataSet.getTable("TAB_ACCOUNT");

            for (int i=0; i<table.getRowCount(); i++) {
                Integer id = (Integer) table.getValue(i, "ACCOUNT_ID");
                if ( id.equals(accountId)) {
                    return true;
                }
            }
        } catch (DataSetException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        return false;
    }

    public static boolean isTweetPresent(int tweetId) {
        IDataSet dataSet = getActualDataSet();
        try {
            ITable table = dataSet.getTable("TAB_TWEET");

            for (int i=0; i<table.getRowCount(); i++) {
                Integer id = (Integer) table.getValue(i, "TWEET_ID");
                if (id.equals(tweetId)) {
                    return true;
                }
            }
        } catch (DataSetException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

        return false;
    }

    public static boolean isUserAFollower(int followerId, int followingId) {
        IDataSet dataSet = getActualDataSet();
        try {
            ITable table = dataSet.getTable("TAB_FOLLOWER");

            for (int i=0; i<table.getRowCount(); i++) {
                Integer follower = (Integer) table.getValue(i, "FOLLOWER_ID");
                Integer following = (Integer) table.getValue(i, "FOLLOWING_ID");
                if (follower.equals(followerId) && following.equals(followingId)) {
                    return true;
                }
            }
        } catch (DataSetException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

        return false;
    }


    public static void initTables() {
        clearTables();
    }

    public static void clearTables () {
        //todo: Löscht nicht die Daten aus TAB_ACCOUNT --> Namen ändern?
        try {
            DatabaseOperation.DELETE_ALL.execute(connection, CustomizedDataSetBuilder.emptyTableDataSet());
        } catch (DatabaseUnitException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
