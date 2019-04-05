package de.openknowledge.playground.api.rest.security.supportCode.datasets;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.openknowledge.playground.api.rest.security.supportCode.converter.convertedClasses.TweetEntity;

import java.util.LinkedList;
import java.util.List;

public class TweetDataSet {
    List<Object> tabLiker;
    List<TweetEntity> tweets;

    public TweetDataSet (List<TweetEntity> tweets) {
        this.tabLiker = new LinkedList<>();
        this.tweets = tweets;
    }


    @JsonProperty("TAB_LIKER")
    public List<Object> getTabLiker() {
        return tabLiker;
    }

    @JsonProperty("TAB_TWEET")
    public List<TweetEntity> getTweets() {
        return tweets;
    }
}
