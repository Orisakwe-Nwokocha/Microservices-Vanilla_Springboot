package dev.orisha.gateway.security.services;

import dev.orisha.gateway.dto.requests.RegisterRequest;
import dev.orisha.gateway.dto.responses.ApiResponse;
import dev.orisha.gateway.dto.responses.RegisterResponse;

public interface AuthService {
    ApiResponse<RegisterResponse> register(RegisterRequest request);
    void blacklist(String token);
    boolean isTokenBlacklisted(String token);
}
