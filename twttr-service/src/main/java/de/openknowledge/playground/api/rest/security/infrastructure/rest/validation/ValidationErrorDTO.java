package de.openknowledge.playground.api.rest.security.infrastructure.rest.validation;

import java.io.Serializable;

public class ValidationErrorDTO implements Serializable{
    private String errorMessage;

    public ValidationErrorDTO(String message) {
        this.errorMessage = message;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
