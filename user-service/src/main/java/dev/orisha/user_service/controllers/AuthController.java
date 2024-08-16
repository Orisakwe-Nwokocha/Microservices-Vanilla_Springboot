package dev.orisha.user_service.controllers;

import dev.orisha.user_service.dto.requests.RegisterRequest;
import dev.orisha.user_service.security.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static dev.orisha.user_service.security.utils.SecurityUtils.JWT_PREFIX;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/users")
public class AuthController {

    private static final String BASE_URL = "/api/v1/auth";

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(BASE_URL + "/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.status(CREATED).body(authService.register(request));
    }

    @PostMapping(BASE_URL + "/logout")
    public ResponseEntity<Void> logout(@RequestHeader(AUTHORIZATION) String token) {
        token = token.replace(JWT_PREFIX, "").strip();
        authService.blacklist(token);
        SecurityContextHolder.clearContext();
        return ResponseEntity.noContent().build();
    }

}
