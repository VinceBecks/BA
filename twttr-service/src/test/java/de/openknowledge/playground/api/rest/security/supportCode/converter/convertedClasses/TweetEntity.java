package de.openknowledge.playground.api.rest.security.supportCode.converter.convertedClasses;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TweetEntity {
    Integer tweetId;
    String conent;
    Date pubDate;
    Integer state;
    Integer authorId;
    String authorName;
    Integer rootTweetId;

    public TweetEntity(Integer tweetId, String conent, Date pubDate, Integer state, String authorName) {
        this.tweetId = tweetId;
        this.conent = conent;
        this.pubDate = new Date(System.currentTimeMillis());
        this.state = state;
        this.authorName = authorName;
    }


    public Integer getTweetId() {
        return tweetId;
    }

    public String getConent() {
        return conent;
    }

    public String getPubDate() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return df.format(pubDate);
    }

    public Integer getState() {
        return state;
    }

    public String getAuthorName() {
        return authorName;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public void setPubDate (Date date) {
        this.pubDate = date;
    }

    public Integer getRootTweetId() {
        return rootTweetId;
    }

    public void setRootTweetId(Integer rootTweetId) {
        this.rootTweetId = rootTweetId;
    }
}