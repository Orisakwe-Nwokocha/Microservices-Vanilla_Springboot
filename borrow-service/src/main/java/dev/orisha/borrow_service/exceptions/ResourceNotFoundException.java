package dev.orisha.borrow_service.exceptions;

public class ResourceNotFoundException extends AppBaseException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
