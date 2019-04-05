package de.openknowledge.playground.api.rest.security.domain.account;

import de.openknowledge.playground.api.rest.security.domain.tweet.Tweet;

import javax.persistence.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Entity
@DiscriminatorValue("USER")
public class User extends Account {

    @ManyToMany(mappedBy = "follows", fetch = FetchType.EAGER)
    private Set<User> follower;

    @ManyToMany (fetch = FetchType.EAGER)
    @JoinTable(name="TAB_FOLLOWER",
            joinColumns = {@JoinColumn(name="FOLLOWER_ID", referencedColumnName = "ACCOUNT_ID")},
            inverseJoinColumns = {@JoinColumn (name="FOLLOWING_ID", referencedColumnName = "ACCOUNT_ID")})
    private Set<User> follows;

    @OneToMany(mappedBy = "author", fetch = FetchType.EAGER)
    private List<Tweet> tweets;

    @ManyToMany(mappedBy = "liker", fetch = FetchType.EAGER)
    private List<Tweet> likes;

    public User () {

    }

    public User (String firstName, String lastName, String userName) {
        super (firstName, lastName, userName);
        this.follower = new HashSet<>();
        this.follows = new HashSet<>();
        this.tweets = new LinkedList<>();
        setRole(AccountType.USER);
    }

    public Set<User> getFollower() {
        return follower;
    }

    public void setFollower(Set<User> follower) {
        this.follower = follower;
    }

    public Set<User> getFollows() {
        return follows;
    }

    public void setFollows(Set<User> follows) {
        this.follows = follows;
    }

    public List<Tweet> getTweets() {
        return tweets;
    }

    public void setTweets(List<Tweet> tweets) {
        this.tweets = tweets;
    }

    public List<Tweet> getLikes() {
        return likes;
    }

    public void setLikes(List<Tweet> likes) {
        this.likes = likes;
    }
}
