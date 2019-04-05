package de.openknowledge.playground.api.rest.security.infrastructure.rest.error;

import javax.validation.Payload;

import static org.apache.commons.lang3.Validate.notNull;

public class ValidationErrorPayload implements Payload{
    private String errorCode;

    protected ValidationErrorPayload(final String errorCode) {
        this.errorCode = notNull(errorCode, "errorCode must not be null");
    }

    public String getErrorCode() {
        return errorCode;
    }
}
