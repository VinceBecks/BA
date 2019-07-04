package de.openknowledge.playground.api.rest.security.application.tweet;

import de.openknowledge.playground.api.rest.security.application.user.UserDTO;
import de.openknowledge.playground.api.rest.security.domain.tweet.Tweet;

import java.io.Serializable;
import java.text.SimpleDateFormat;

public class TweetDTO implements Serializable {
    private Integer tweetId;
    private String content;
    private UserDTO author;
    private TweetDTO rootTweet;
    private String pubDate;

    public TweetDTO () {
        //for JaxRs
    }

    public TweetDTO (Tweet tweet) {
        this.tweetId = tweet.getTweetId();
        this.content = tweet.getContent().getContent();
        this.pubDate = new SimpleDateFormat().format(tweet.getPubDate().getPubDate());
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

    public String getPubDate() { return pubDate; }

    public void setPubDate(String pubDate) { this.pubDate = pubDate; }
}
