package de.openknowledge.playground.api.rest.security.domain.tweet;


import de.openknowledge.playground.api.rest.security.domain.account.User;
import org.apache.commons.lang3.Validate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "TAB_TWEET")
public class Tweet implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TWEET_ID")
    private Integer tweetId;

    @Column(name = "CONTENT", nullable = false)
    private String content;

    @Enumerated
    @Column(name = "STATE", nullable = false)
    private TweetState state;

    @ManyToOne
    @JoinColumn(name = "AUTHOR")
    private User author;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="TAB_LIKER",
            joinColumns = {@JoinColumn (name="TWEET_ID", referencedColumnName = "TWEET_ID")},
            inverseJoinColumns = {@JoinColumn (name="USER_ID", referencedColumnName = "ACCOUNT_ID")})
    private List<User> liker;

    @ManyToOne
    @JoinColumn(name="ROOT_TWEET")
    private Tweet rootTweet;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "rootTweet", fetch = FetchType.EAGER)
    private List<Tweet> retweets;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "PUBLISH_DATE")
    private Date pubDate;

    private Tweet() {

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

    public TweetState getState() {
        return state;
    }

    public void setState(TweetState state) {
        this.state = state;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public List<User> getLiker() {
        return liker;
    }

    public void setLiker(List<User> liker) {
        this.liker = liker;
    }

    public List<Tweet> getRetweets() {
        return retweets;
    }

    public void setRetweets(List<Tweet> retweets) {
        this.retweets = retweets;
    }

    public Tweet getRootTweet() {
        return rootTweet;
    }

    public void setRootTweet(Tweet rootTweet) {
        this.rootTweet = rootTweet;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

    public static Builder newTweet() { return new Builder(); }

    public static class Builder {
        Tweet tweet;
        Builder(){
            tweet = new Tweet();
            tweet.setRetweets(new LinkedList<>());
            tweet.setLiker(new LinkedList<>());
            tweet.setPubDate(new Date(System.currentTimeMillis()));
            tweet.setState(TweetState.PUBLISH);
        }

        public Builder withContent(String content) {
            tweet.setContent(content);
            return this;
        }

        public Builder withState(TweetState state) {
            tweet.setState(state);
            return this;
        }

        public Builder withAuthor(User author) {
            tweet.setAuthor(author);
            return this;
        }

        public Builder withLikes(LinkedList<User> liker) {
            tweet.setLiker(liker);
            return this;
        }

        public Builder withRetweets(LinkedList<Tweet> retweets) {
            tweet.setRetweets(retweets);
            return this;
        }

        public Builder withRootTweet(Tweet rootTweet){
            tweet.setRootTweet(rootTweet);
            return this;
        }

        public Builder withPubDate(Date pubDate) {
            tweet.setPubDate(pubDate);
            return this;
        }

        public Tweet build(){
            Validate.notNull(tweet.getContent());
            Validate.notNull(tweet.getAuthor());
            Validate.notNull(tweet.getPubDate());
            Validate.notNull(tweet.getState());
            Validate.notNull(tweet.getLiker());
            Validate.notNull(tweet.getRetweets());
            Tweet toBuild = tweet;
            tweet = null;
            return toBuild;
        }
    }


}
