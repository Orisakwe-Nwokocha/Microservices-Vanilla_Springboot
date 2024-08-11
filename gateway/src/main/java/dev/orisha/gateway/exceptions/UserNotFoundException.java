package dev.orisha.gateway.exceptions;

public class UserNotFoundException extends AppBaseException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
