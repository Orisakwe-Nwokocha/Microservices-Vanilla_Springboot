package dev.orisha.gateway.exceptions;

public class EmailExistsException extends AppBaseException {
    public EmailExistsException(String message) {
        super(message);
    }
}
