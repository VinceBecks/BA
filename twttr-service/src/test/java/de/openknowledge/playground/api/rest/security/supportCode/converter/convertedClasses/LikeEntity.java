package de.openknowledge.playground.api.rest.security.supportCode.converter.convertedClasses;

public class LikeEntity {
    private Integer tweetId, userId;

    public LikeEntity(Integer tweetId, Integer userId) {
        this.tweetId = tweetId;
        this.userId = userId;
    }

    public Integer getTweetId() {
        return tweetId;
    }

    public void setTweetId(Integer tweetId) {
        this.tweetId = tweetId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
