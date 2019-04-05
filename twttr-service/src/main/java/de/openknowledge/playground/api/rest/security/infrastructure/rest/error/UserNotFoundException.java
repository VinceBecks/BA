package de.openknowledge.playground.api.rest.security.infrastructure.rest.error;

import javax.ws.rs.NotFoundException;

public class UserNotFoundException extends NotFoundException {

    private Integer userId;
    private String userName;

    public UserNotFoundException (final Integer userId) {
        this.userId = userId;
    }

    public UserNotFoundException (final Integer userId, Throwable throwable) {
        super (throwable);
        this.userId = userId;
    }

    public UserNotFoundException (final String userName) {
        this.userName = userName;
    }

    public UserNotFoundException (final String userName, Throwable throwable) {
        super (throwable);
        this.userName = userName;
    }

    @Override
    public String getMessage () {
        if (userId != null){
            return String.format("User with id %s was not found", this.userId);
        }else {
            return String.format("User with userName %s was not found", this.userName);
        }
    }

    public Integer getUserId () {
        return this.userId;
    }

    public String getUserName() {
        return userName;
    }
}
