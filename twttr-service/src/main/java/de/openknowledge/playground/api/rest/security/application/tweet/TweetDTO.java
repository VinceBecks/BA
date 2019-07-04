package de.openknowledge.playground.api.rest.security.application.tweet;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.openknowledge.playground.api.rest.security.application.user.UserDTO;
import de.openknowledge.playground.api.rest.security.domain.tweet.Tweet;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TweetDTO implements Serializable {
    private Integer tweetId;
    private String content;
    private UserDTO author;
    private TweetDTO rootTweet;
    @JsonIgnore
    private Date pubDate;

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

    @JsonProperty(value = "pubDate")
    public String getPubDateAsString() {
        DateFormat df = new SimpleDateFormat();
        return df.format(pubDate);
    }

}
