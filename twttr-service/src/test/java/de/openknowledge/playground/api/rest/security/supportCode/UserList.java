package de.openknowledge.playground.api.rest.security.supportCode;

        import com.fasterxml.jackson.annotation.JsonProperty;
        import de.openknowledge.playground.api.rest.security.supportCode.converter.convertedClasses.Account;

        import java.util.LinkedList;
        import java.util.List;

public class UserList {
    List<Object> tabFollower;
    List<Object> tabLiker;
    List<Object> tabTweet;
    List<Account> accounts;

    public UserList(List<Account> accounts) {
        this.accounts = accounts;
        this.tabFollower = new LinkedList<>();
        this.tabLiker= new LinkedList<>();
        this.tabTweet= new LinkedList<>();
    }

    @JsonProperty("TAB_ACCOUNT")
    public List<Account> getAccounts() {
        return accounts;
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
