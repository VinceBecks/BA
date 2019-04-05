package de.openknowledge.playground.api.rest.security.supportCode.datasets;

        import com.fasterxml.jackson.annotation.JsonProperty;
        import de.openknowledge.playground.api.rest.security.supportCode.converter.convertedClasses.AccountEntity;

        import java.util.LinkedList;
        import java.util.List;

public class AccountDataSet {
    List<Object> tabFollower;
    List<Object> tabLiker;
    List<Object> tabTweet;
    List<AccountEntity> accountEntities;

    public AccountDataSet(List<AccountEntity> accountEntities) {
        this.accountEntities = accountEntities;
        this.tabFollower = new LinkedList<>();
        this.tabLiker= new LinkedList<>();
        this.tabTweet= new LinkedList<>();
    }

    @JsonProperty("TAB_ACCOUNT")
    public List<AccountEntity> getAccountEntities() {
        return accountEntities;
    }

    @JsonProperty("TAB_LIKER")
    public List<Object> getTabFollower() {
        return tabFollower;
    }

    @JsonProperty("TAB_FOLLOWER")
    public List<Object> getTabLiker() {
        return tabLiker;
    }

    @JsonProperty("TAB_TWEET")
    public List<Object> getTabTweet() {
        return tabTweet;
    }
}
