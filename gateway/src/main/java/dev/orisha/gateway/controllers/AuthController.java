package dev.orisha.gateway.controllers;

import dev.orisha.gateway.dto.requests.RegisterRequest;
import dev.orisha.gateway.security.services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static dev.orisha.gateway.security.utils.SecurityUtils.JWT_PREFIX;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.status(CREATED).body(authService.register(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith(JWT_PREFIX)) {
            String token = authHeader.replace(JWT_PREFIX, "").strip();
            authService.blacklist(token);
            SecurityContextHolder.clearContext();
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.badRequest().build();
    }

}
