package dev.orisha.gateway.exceptions;

public abstract class AppBaseException extends RuntimeException {
    public AppBaseException(String message){
        super(message);
    }
}
