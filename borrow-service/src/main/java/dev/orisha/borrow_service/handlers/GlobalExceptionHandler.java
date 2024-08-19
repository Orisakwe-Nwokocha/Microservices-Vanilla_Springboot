package dev.orisha.borrow_service.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.orisha.borrow_service.dto.responses.ErrorResponse;
import dev.orisha.borrow_service.exceptions.ResourceNotFoundException;
import feign.FeignException;
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
        ErrorResponse response = buildErrorResponse("IllegalState", exception.getMessage(), request);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<?> handleIllegalStateException(IllegalStateException exception, HttpServletRequest request) {
        ErrorResponse response = buildErrorResponse("IllegalState", exception.getMessage(), request);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<?> handleIOException(IOException exception, HttpServletRequest request) {
        ErrorResponse response = buildErrorResponse("UploadMediaFailed", exception.getMessage(), request);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException exception, HttpServletRequest request) {
        ErrorResponse response = buildErrorResponse("ResourceNotFound", exception.getMessage(), request);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolationException(DataIntegrityViolationException exception, HttpServletRequest request) {
        ErrorResponse response = buildErrorResponse("BadRequest", exception.getMessage(), request);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<?> handleHttpClientErrorException(HttpClientErrorException exception,
                                                            HttpServletRequest request) {
        ErrorResponse response = buildErrorResponse("ClientError", exception.getMessage(), request);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<?> handleFeignException(FeignException exception, HttpServletRequest request) {
        String message = extractErrorMessage(exception);
        log.error("Feign error: {}", message);
        ErrorResponse response = buildErrorResponse("FeignClientError", message, request);
        return ResponseEntity.badRequest().body(response);
    }

    private String extractErrorMessage(FeignException exception) {
        String content = exception.contentUTF8();
        try {
            JsonNode jsonNode = new ObjectMapper().readTree(content);
            return jsonNode.get("message").asText();
        } catch (JsonProcessingException e) {
            throw new ResourceNotFoundException("Error message not found");
        }
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

}
