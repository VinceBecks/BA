package de.openknowledge.twttrService.api.rest.supportCode.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.apache.commons.lang3.Validate.notNull;

public class TweetEntity {
    Integer tweetId;
    String conent;
    Date pubDate;
    Integer state;
    Integer authorId;
    Integer rootTweetId;

    private TweetEntity () {
    }

    private TweetEntity(Integer tweetId, String conent, Date pubDate, Integer state, Integer authorId) {
        this.tweetId = notNull(tweetId, "tweetId should not be null");
        this.conent = notNull(conent, "content should not be null");
        this.pubDate = pubDate != null ? pubDate : new Date(System.currentTimeMillis());
        this.state = notNull(state, "state should not be null");
        this.authorId = notNull(authorId, "authorId should not be null");
    }

    public Integer getTweetId() {
        return tweetId;
    }

    public String getConent() {
        return conent;
    }

    public String getPubDate() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return df.format(pubDate);
    }

    public Integer getState() {
        return state;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public void setPubDate (Date date) {
        this.pubDate = date;
    }

    public Integer getRootTweetId() {
        return rootTweetId;
    }

    public void setRootTweetId(Integer rootTweetId) {
        this.rootTweetId = rootTweetId;
    }

    public static TweetEntity.Builder builderInstance(){
        return new TweetEntity.Builder();
    }


    public static class Builder {
        Integer tweetId;
        String content;
        Integer state;
        Integer authorId;
        Date pubDate = null;
        Integer rootTweetId = null;

        TweetEntity entity;

        public Builder() { entity = new TweetEntity(); }

        public Builder withTweetId(Integer tweetId) {
            this.tweetId = tweetId;
            return this;
        }

        public Builder withContent(String content) {
            this.content = content;
            return this;
        }

        public Builder withState(Integer state) {
            this.state = state;
            return this;
        }

        public Builder withAuthorId(Integer authorId) {
            this.authorId = authorId;
            return this;
        }

        public Builder withPubDate(Date pubDate) {
            this.pubDate = pubDate;
            return this;
        }

        public Builder withRootTweetId(Integer rootTweetId) {
            this.rootTweetId = rootTweetId;
            return this;
        }

        public TweetEntity build() {
            notNull(this.tweetId, "TweetId should not be null");
            notNull(this.content, "Content should not be null");
            notNull(this.state, "State should not be null");
            notNull(this.authorId, "AuthorId should not be null");

            entity = new TweetEntity(tweetId, content, pubDate, state, authorId);
            entity.setRootTweetId(rootTweetId);
            entity.setPubDate(pubDate);
            TweetEntity build = entity;
            entity = new TweetEntity();
            return build;
        }
    }
}