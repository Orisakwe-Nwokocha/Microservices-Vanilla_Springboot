package dev.orisha.user_service.handlers;

import dev.orisha.user_service.dto.responses.ErrorResponse;
import dev.orisha.user_service.exceptions.EmailExistsException;
import dev.orisha.user_service.exceptions.ResourceNotFoundException;
import dev.orisha.user_service.exceptions.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static java.time.LocalDateTime.now;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<?> handleNullPointerException(NullPointerException exception, HttpServletRequest request) {
        log(exception.getMessage());
        ErrorResponse response = buildErrorResponse("IllegalState", exception.getMessage(), request);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<?> handleIllegalStateException(IllegalStateException exception, HttpServletRequest request) {
        log(exception.getMessage());
        ErrorResponse response = buildErrorResponse("IllegalState", exception.getMessage(), request);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(EmailExistsException.class)
    public ResponseEntity<?> handleEmailExistsException(EmailExistsException exception, HttpServletRequest request) {
        log(exception.getMessage());
        ErrorResponse response = buildErrorResponse("EmailExists", exception.getMessage(), request);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException exception, HttpServletRequest request) {
        log(exception.getMessage());
        ErrorResponse response = buildErrorResponse("UserNotFound", exception.getMessage(), request);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException exception, HttpServletRequest request) {
        log(exception.getMessage());
        ErrorResponse response = buildErrorResponse("ResourceNotFound", exception.getMessage(), request);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolationException(DataIntegrityViolationException exception,
                                                                   HttpServletRequest request) {
        log(exception.getMessage());
        ErrorResponse response = buildErrorResponse("BadRequest", "Invalid input", request);
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

    private static void log(String exceptionMessage) {
        log.error("ERROR: {}", exceptionMessage);
    }

}
