package de.openknowledge.playground.api.rest.security.domain.tweet;

import java.io.Serializable;

public class NewTweet implements Serializable {
    private String content;

    public NewTweet() {
        //for REST
    }

    public NewTweet(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
