package de.openknowledge.twttrService.api.rest.infrastructure.rest.error;

public class TweetValidationErrorPayload {

    public static class TweetHasWrongSize extends ValidationErrorPayload {
        public TweetHasWrongSize() {
            super("Content size must be between 1 and 140");
        }
    }

    public static class ContentIsNull extends ValidationErrorPayload {
        public ContentIsNull() { super("Content must not be null"); }
    }
}
