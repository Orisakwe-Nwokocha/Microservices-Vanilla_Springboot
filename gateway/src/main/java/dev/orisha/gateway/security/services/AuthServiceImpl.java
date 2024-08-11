package dev.orisha.gateway.security.services;

import dev.orisha.gateway.data.models.User;
import dev.orisha.gateway.data.repositories.UserRepository;
import dev.orisha.gateway.dto.requests.RegisterRequest;
import dev.orisha.gateway.dto.responses.ApiResponse;
import dev.orisha.gateway.dto.responses.RegisterResponse;
import dev.orisha.gateway.exceptions.EmailExistsException;
import dev.orisha.gateway.security.data.models.BlacklistedToken;
import dev.orisha.gateway.security.data.repositories.BlacklistedTokenRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;

import static java.time.Instant.now;
import static java.time.temporal.ChronoUnit.HOURS;

@Service
@AllArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final BlacklistedTokenRepository blacklistedTokenRepository;

    @Override
    public ApiResponse<RegisterResponse> register(RegisterRequest request) {
        log.info("Registering new user");
        validateExistingEmail(request.getEmail());
        User newUser = createAndSaveUser(request);
        RegisterResponse response = modelMapper.map(newUser, RegisterResponse.class);
        response.setMessage("Successfully registered");
        log.info("User successfully registered with authorities: {}", newUser.getAuthorities());
        return new ApiResponse<>(LocalDateTime.now(), true, response);
    }


    @Override
    public void blacklist(String token) {
        log.info("Trying to blacklist token: {}", token);
        trackExpiredTokens();
        BlacklistedToken blacklistedToken = new BlacklistedToken();
        blacklistedToken.setToken(token);
        blacklistedToken.setExpiresAt(now().plus(24, HOURS));
        blacklistedTokenRepository.save(blacklistedToken);
        log.info("Blacklisted token: {}", token);
    }

    @Override
    public boolean isTokenBlacklisted(String token) {
        log.info("Checking blacklist status of token: {}", token);
        boolean isBlacklisted = blacklistedTokenRepository.existsByToken(token);
        log.info("Blacklist status of token: {}", isBlacklisted);
        trackExpiredTokens();
        return isBlacklisted;
    }

    private void trackExpiredTokens() {
        log.info("Tracking and deleting expired user tokens");
        var blacklist = blacklistedTokenRepository.findAll();
        blacklist.stream()
                .filter(blacklistedToken -> now().isAfter(blacklistedToken.getExpiresAt()))
                .forEach(blacklistedTokenRepository::delete);
        log.info("Expired user tokens successfully tracked and deleted");
    }

    private User createAndSaveUser(RegisterRequest request) {
        User newUser = modelMapper.map(request, User.class);
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        newUser.setAuthorities(new HashSet<>());
        newUser.getAuthorities().add(request.getAuthority());
        return userRepository.save(newUser);
    }

    private void validateExistingEmail(String email) {
        boolean emailExists = userRepository.existsByEmail(email);
        if (emailExists) throw new EmailExistsException(email + " already exists");
    }

}
