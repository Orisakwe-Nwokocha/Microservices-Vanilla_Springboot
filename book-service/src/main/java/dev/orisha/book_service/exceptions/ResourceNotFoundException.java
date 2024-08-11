package dev.orisha.book_service.exceptions;

public class ResourceNotFoundException extends AppBaseException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
