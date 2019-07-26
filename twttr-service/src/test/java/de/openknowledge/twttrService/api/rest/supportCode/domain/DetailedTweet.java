package de.openknowledge.twttrService.api.rest.supportCode.domain;

import de.openknowledge.twttrService.api.rest.application.tweet.TweetDTO;
import de.openknowledge.twttrService.api.rest.application.user.UserDTO;
import de.openknowledge.twttrService.api.rest.domain.tweet.Tweet;

import java.io.Serializable;
import java.text.SimpleDateFormat;

public class DetailedTweet implements Serializable{
    private Integer tweetId;
    private String content;
    private String pubDate;
    private de.openknowledge.twttrService.api.rest.application.user.UserDTO author;
    private Integer numRetweets;
    private Integer numLiker;
    private de.openknowledge.twttrService.api.rest.application.tweet.TweetDTO rootTweet;


    public DetailedTweet () {
        // for JaxRs
    }

    public DetailedTweet (Tweet tweet) {
        this.tweetId = tweet.getTweetId();
        this.content = tweet.getContent().getContent();
        this.pubDate = new SimpleDateFormat().format(tweet.getPubDate().getPubDate());
        this.author = new de.openknowledge.twttrService.api.rest.application.user.UserDTO(tweet.getAuthor());
        this.numLiker = tweet.getLiker().size();
        this.numRetweets = tweet.getRetweets().size();
        this.rootTweet = tweet.getRootTweet() != null ? new de.openknowledge.twttrService.api.rest.application.tweet.TweetDTO(tweet.getRootTweet()) : null;
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

    public de.openknowledge.twttrService.api.rest.application.user.UserDTO getAuthor() {
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

    public de.openknowledge.twttrService.api.rest.application.tweet.TweetDTO getRootTweet() {
        return rootTweet;
    }

    public void setRootTweet(TweetDTO rootTweet) {
        this.rootTweet = rootTweet;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }
}
