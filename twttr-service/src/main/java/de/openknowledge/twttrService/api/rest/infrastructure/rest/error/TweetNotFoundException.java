package de.openknowledge.twttrService.api.rest.infrastructure.rest.error;

import javax.ws.rs.NotFoundException;

public class TweetNotFoundException extends NotFoundException {
    private final Integer tweetId;

    public TweetNotFoundException (final Integer tweetId) {
        this.tweetId = tweetId;
    }

    public TweetNotFoundException (final Integer tweetId, Throwable throwable) {
        super (throwable);
        this.tweetId = tweetId;
    }

    @Override
    public String getMessage () {
        return String.format("Tweet with id %s was not found", this.tweetId);
    }

    public Integer getTweetId () {
        return this.tweetId;
    }
}
