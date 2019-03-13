package de.openknowledge.playground.api.rest.security.domain.tweet;

import de.openknowledge.playground.api.rest.security.domain.accounts.UserDTO;

import java.io.Serializable;
import java.util.Date;

public class DetailedTweet implements Serializable{

    private Integer tweetId;
    private String content;
    private Date pubDate;
    private UserDTO author;
    private Integer numLiker;
    private Integer numRetweets;

    public DetailedTweet () {
        // for REST
    }

    public Integer getTweetId() {
        return tweetId;
    }

    public void setTweetId(Integer tweetId) {
        this.tweetId = tweetId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

    public UserDTO getAuthor() {
        return author;
    }

    public void setAuthor(UserDTO author) {
        this.author = author;
    }

    public Integer getNumLiker() {
        return numLiker;
    }

    public void setNumLiker(Integer numLiker) {
        this.numLiker = numLiker;
    }

    public Integer getNumRetweets() {
        return numRetweets;
    }

    public void setNumRetweets(Integer numRetweets) {
        this.numRetweets = numRetweets;
    }
}
