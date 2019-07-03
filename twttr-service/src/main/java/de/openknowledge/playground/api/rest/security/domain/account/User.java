package de.openknowledge.playground.api.rest.security.domain.account;

import de.openknowledge.playground.api.rest.security.domain.tweet.Tweet;
import org.apache.commons.lang3.Validate;

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

    private User () {

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

    public static Builder newUser() {
        return new Builder();
    }

    public static class Builder {
        User user;

        private Builder(){
            user = new User();
        }

        public Builder withName(Name name) {
            user.setName(name);
            return this;
        }

        public User build() {
            Validate.notNull(user.getName());
            User toBuild = user;
            user = null;
            return toBuild;
        }
    }
}
