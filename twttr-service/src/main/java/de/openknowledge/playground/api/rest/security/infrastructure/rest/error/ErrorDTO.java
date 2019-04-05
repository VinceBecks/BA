package de.openknowledge.playground.api.rest.security.infrastructure.rest.error;

import javax.ws.rs.NotFoundException;
import java.io.Serializable;

public class ErrorDTO implements Serializable{
    private String errorMessage;

    public ErrorDTO () {
        //for JaxRs
    }

    public ErrorDTO (NotFoundException e) {
        this.errorMessage = e.getMessage();
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
