package de.openknowledge.twttrService.api.rest.domain.tweet;

import javax.persistence.Embeddable;

@Embeddable
public class Content {
    private String content;

    public Content() {
        //for JPA
    }

    public Content(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public static boolean isValid(String content) {
        return content != null && content.length() <= 140;
    }
}
