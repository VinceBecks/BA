package de.openknowledge.playground.api.rest.security.application.tweet;

import de.openknowledge.playground.api.rest.security.application.user.UserDTO;
import de.openknowledge.playground.api.rest.security.domain.tweet.Tweet;

import java.io.Serializable;
import java.util.Date;

public class DetailedTweet implements Serializable {
    private Integer tweetId;
    private String content;
    private Date pubDate;
    private UserDTO author;
    private Integer numRetweets;
    private Integer numLiker;
    private TweetDTO rootTweet;


    public DetailedTweet () {
        // for JaxRs
    }

    public DetailedTweet (Tweet tweet) {
        this.tweetId = tweet.getTweetId();
        this.content = tweet.getContent().getContent();
        this.pubDate = tweet.getPubDate().getPubDate();
        this.author = new UserDTO(tweet.getAuthor());
        this.numLiker = tweet.getLiker().size();
        this.numRetweets = tweet.getRetweets().size();
        this.rootTweet = tweet.getRootTweet() != null ? new TweetDTO(tweet.getRootTweet()) : null;
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

    public Integer getNumRetweets() {
        return numRetweets;
    }

    public void setNumRetweets(Integer numRetweets) {
        this.numRetweets = numRetweets;
    }

    public Integer getNumLiker() {
        return numLiker;
    }

    public void setNumLiker(Integer numLiker) {
        this.numLiker = numLiker;
    }

    public TweetDTO getRootTweet() {
        return rootTweet;
    }

    public void setRootTweet(TweetDTO rootTweet) {
        this.rootTweet = rootTweet;
    }
}
