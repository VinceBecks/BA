package de.openknowledge.playground.api.rest.security.application.tweet;

import de.openknowledge.playground.api.rest.security.infrastructure.rest.error.TweetValidationErrorPayload;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class NewTweet implements Serializable {
    @Schema (example="Exmaple content", required = true, minLength = 1, maxLength = 140)
    @NotNull(payload = TweetValidationErrorPayload.ContentIsNull.class, message ="Content not setted")
    @Size(min=1, max=140, payload = TweetValidationErrorPayload.TweetHasWrongSize.class, message = "Wrong number of character")
    private String content;

    public NewTweet() {
        //for JPA
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
