package dev.orisha.gateway.handlers;

import dev.orisha.gateway.dto.responses.ErrorResponse;
import dev.orisha.gateway.exceptions.EmailExistsException;
import dev.orisha.gateway.exceptions.ResourceNotFoundException;
import dev.orisha.gateway.exceptions.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;

import static java.time.LocalDateTime.now;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<?> handleNullPointerException(NullPointerException exception, HttpServletRequest request) {
        logExceptionMessage(exception.getMessage());
        ErrorResponse response = buildErrorResponse("IllegalState", exception.getMessage(), request);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<?> handleIllegalStateException(IllegalStateException exception, HttpServletRequest request) {
        logExceptionMessage(exception.getMessage());
        ErrorResponse response = buildErrorResponse("IllegalState", exception.getMessage(), request);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(EmailExistsException.class)
    public ResponseEntity<?> handleEmailExistsException(EmailExistsException exception, HttpServletRequest request) {
        logExceptionMessage(exception.getMessage());
        ErrorResponse response = buildErrorResponse("EmailExists", exception.getMessage(), request);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<?> handleIOException(IOException exception, HttpServletRequest request) {
        logExceptionMessage(exception.getMessage());
        ErrorResponse response = buildErrorResponse("UploadMediaFailed", exception.getMessage(), request);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException exception, HttpServletRequest request) {
        logExceptionMessage(exception.getMessage());
        ErrorResponse response = buildErrorResponse("UserNotFound", exception.getMessage(), request);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException exception, HttpServletRequest request) {
        logExceptionMessage(exception.getMessage());
        ErrorResponse response = buildErrorResponse("ResourceNotFound", exception.getMessage(), request);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolationException(DataIntegrityViolationException exception,
                                                                   HttpServletRequest request) {
        logExceptionMessage(exception.getMessage());
        ErrorResponse response = buildErrorResponse("BadRequest", exception.getMessage(), request);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<?> handleHttpClientErrorException(HttpClientErrorException exception,
                                                            HttpServletRequest request) {
        logExceptionMessage(exception.getMessage());
        ErrorResponse response = buildErrorResponse("ClientError", exception.getMessage(), request);
        return ResponseEntity.badRequest().body(response);
    }

    private static ErrorResponse buildErrorResponse(String error, String message, HttpServletRequest request) {
        return ErrorResponse.builder()
                .responseTime(now())
                .isSuccessful(false)
                .error(error)
                .message(message)
                .path(request.getRequestURI())
                .build();
    }

    private static void logExceptionMessage(String exceptionMessage) {
        log.error("ERROR: {}", exceptionMessage);
    }

}
