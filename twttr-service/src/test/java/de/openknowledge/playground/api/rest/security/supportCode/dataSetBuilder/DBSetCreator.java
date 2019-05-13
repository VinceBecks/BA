package de.openknowledge.playground.api.rest.security.supportCode.dataSetBuilder;

import com.github.database.rider.core.api.dataset.DataSetExecutor;
import com.github.database.rider.core.configuration.DataSetConfig;
import com.github.database.rider.core.exception.DataBaseSeedingException;
import com.github.database.rider.core.replacers.Replacer;
import de.openknowledge.playground.api.rest.security.supportCode.converter.convertedClasses.AccountEntity;
import de.openknowledge.playground.api.rest.security.supportCode.converter.convertedClasses.FollowerEntity;
import de.openknowledge.playground.api.rest.security.supportCode.converter.convertedClasses.LikeEntity;
import de.openknowledge.playground.api.rest.security.supportCode.converter.convertedClasses.TweetEntity;
import de.openknowledge.playground.api.rest.security.supportCode.dataSetBuilder.CustomizedDataSetBuilder;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseSequenceFilter;
import org.dbunit.dataset.FilteredDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.filter.ITableFilter;
import org.dbunit.operation.DatabaseOperation;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class DBSetCreator {

    private DataSetExecutor dbExecutor;

    public DBSetCreator(DataSetExecutor dbExecutor) {
        this.dbExecutor = dbExecutor;
    }


    public void createTweetDataSet(DataSetConfig dataSetConfig, List<TweetEntity> tweets) {
        if (dataSetConfig != null) {
            try {
                IDataSet resultingDataSet = new CustomizedDataSetBuilder().createTweets(tweets);
                resultingDataSet = performSequenceFiltering(dataSetConfig, resultingDataSet);
                resultingDataSet = performReplacements(resultingDataSet);
                DatabaseOperation operation = dataSetConfig.getstrategy().getOperation();
                operation.execute(dbExecutor.getRiderDataSource().getDBUnitConnection(), resultingDataSet);
            } catch (Exception e) {
                throw new DataBaseSeedingException("Could not initialize dataset: " + dataSetConfig, e);
            }
        }
    }


    public void createLikesDataSet(DataSetConfig dataSetConfig, List<LikeEntity> likes) {
        if (dataSetConfig != null) {
            try {
                IDataSet resultingDataSet = new CustomizedDataSetBuilder().createLikes(likes);
                resultingDataSet = performSequenceFiltering(dataSetConfig, resultingDataSet);
                resultingDataSet = performReplacements(resultingDataSet);
                DatabaseOperation operation = dataSetConfig.getstrategy().getOperation();
                operation.execute(dbExecutor.getRiderDataSource().getDBUnitConnection(), resultingDataSet);
            } catch (Exception e) {
                throw new DataBaseSeedingException("Could not initialize dataset: " + dataSetConfig, e);
            }
        }
    }


    public void createFollowerDataSet(DataSetConfig dataSetConfig, List<FollowerEntity> follower) {
        if (dataSetConfig != null) {
            try {
                IDataSet resultingDataSet = new CustomizedDataSetBuilder().createFollower(follower);
                resultingDataSet = performSequenceFiltering(dataSetConfig, resultingDataSet);
                resultingDataSet = performReplacements(resultingDataSet);
                DatabaseOperation operation = dataSetConfig.getstrategy().getOperation();
                operation.execute(dbExecutor.getRiderDataSource().getDBUnitConnection(), resultingDataSet);
            } catch (Exception e) {
                throw new DataBaseSeedingException("Could not initialize dataset: " + dataSetConfig, e);
            }
        }
    }

    public void createAccountDataSet(DataSetConfig dataSetConfig, List<AccountEntity> accounts) {
        if (dataSetConfig != null) {
            try {
                IDataSet resultingDataSet = new CustomizedDataSetBuilder().createAccounts(accounts);
                resultingDataSet = performSequenceFiltering(dataSetConfig, resultingDataSet);
                resultingDataSet = performReplacements(resultingDataSet);
                DatabaseOperation operation = dataSetConfig.getstrategy().getOperation();
                operation.execute(dbExecutor.getRiderDataSource().getDBUnitConnection(), resultingDataSet);
            } catch (Exception e) {
                throw new DataBaseSeedingException("Could not initialize dataset: " + dataSetConfig, e);
            }
        }
    }

    private IDataSet performSequenceFiltering(DataSetConfig dataSet, IDataSet target)
            throws DatabaseUnitException, SQLException {
        if (dataSet.isUseSequenceFiltering()) {
            ITableFilter filteredTable = new DatabaseSequenceFilter(dbExecutor.getRiderDataSource().getDBUnitConnection(),
                    target.getTableNames());
            target = new FilteredDataSet(filteredTable, target);
        }
        return target;
    }



    private IDataSet performReplacements(IDataSet dataSet) {
        if (!dbExecutor.getDBUnitConfig().getProperties().containsKey("replacers")) {
            return dataSet;
        }
        return performReplacements(dataSet,(List<Replacer>) dbExecutor.getDBUnitConfig().getProperties().get("replacers"));
    }

    private IDataSet performReplacements(IDataSet dataSet, List<Replacer> replacersList) {
        if (replacersList==null || replacersList.isEmpty())
            return dataSet;

        ReplacementDataSet replacementSet = new ReplacementDataSet(dataSet);
        // convert to set to remove duplicates
        Set<Replacer> replacers = new HashSet<>((List<Replacer>)replacersList);
        for (Replacer replacer : replacers) {
            replacer.addReplacements(replacementSet);
        }

        return replacementSet;
    }

}
