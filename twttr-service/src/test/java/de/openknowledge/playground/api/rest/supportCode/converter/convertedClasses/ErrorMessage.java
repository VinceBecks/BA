package de.openknowledge.playground.api.rest.supportCode.converter.convertedClasses;

import java.io.Serializable;

public class ErrorMessage implements Serializable{
    private String errorMessage;

    public ErrorMessage () {
        //for REST
    }

    public ErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}