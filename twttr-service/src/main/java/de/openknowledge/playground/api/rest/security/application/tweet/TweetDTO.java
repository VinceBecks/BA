package de.openknowledge.playground.api.rest.security.application.tweet;

import de.openknowledge.playground.api.rest.security.application.user.UserDTO;
import de.openknowledge.playground.api.rest.security.domain.tweet.Tweet;

import java.io.Serializable;
import java.util.Date;

public class TweetDTO implements Serializable {
    private Integer tweetId;
    private String content;
    private Date pubDate;
    private UserDTO author;
    private TweetDTO rootTweet;

    public TweetDTO () {
        //for JaxRs
    }

    public TweetDTO (Tweet tweet) {
        this.tweetId = tweet.getTweetId();
        this.content = tweet.getContent().getContent();
        this.pubDate = tweet.getPubDate().getPubDate();
        this.author = new UserDTO(tweet.getAuthor());
        if (tweet.getRootTweet() != null) { this.rootTweet = new TweetDTO(tweet.getRootTweet()); }
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

    public TweetDTO getRootTweet() {
        return rootTweet;
    }

    public void setRootTweet(TweetDTO rootTweet) {
        this.rootTweet = rootTweet;
    }
}
