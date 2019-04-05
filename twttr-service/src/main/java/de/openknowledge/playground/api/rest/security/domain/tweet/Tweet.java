package de.openknowledge.playground.api.rest.security.domain.tweet;


import de.openknowledge.playground.api.rest.security.domain.account.User;

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

    public Tweet() {
        //for JPA
    }

    public Tweet (String content, User author) {
        this.content = content;
        this.author = author;
        this.state = TweetState.PUBLISH;
        this.liker = new LinkedList<>();
        this.retweets = new LinkedList<>();
        this.pubDate = new Date(System.currentTimeMillis());
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


}
