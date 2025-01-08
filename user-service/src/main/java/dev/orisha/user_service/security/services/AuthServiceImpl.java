package dev.orisha.user_service.security.services;

import dev.orisha.user_service.data.mappers.UserMapper;
import dev.orisha.user_service.data.models.User;
import dev.orisha.user_service.data.repositories.UserRepository;
import dev.orisha.user_service.dto.requests.RegisterRequest;
import dev.orisha.user_service.dto.responses.ApiResponse;
import dev.orisha.user_service.dto.responses.RegisterResponse;
import dev.orisha.user_service.exceptions.EmailExistsException;
import dev.orisha.user_service.security.data.models.BlacklistedToken;
import dev.orisha.user_service.security.data.repositories.BlacklistedTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static java.time.Instant.now;
import static java.time.temporal.ChronoUnit.HOURS;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final BlacklistedTokenRepository blacklistedTokenRepository;
    private final UserMapper userMapper;

    @Autowired
    public AuthServiceImpl(final UserRepository userRepository,
                           final ModelMapper modelMapper,
                           final PasswordEncoder passwordEncoder,
                           final BlacklistedTokenRepository blacklistedTokenRepository,
                           final UserMapper userMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.blacklistedTokenRepository = blacklistedTokenRepository;
        this.userMapper = userMapper;
    }


    @Override
    public ApiResponse<RegisterResponse> register(RegisterRequest request) {
//        if (request != null) {
//            throw new NullPointerException("Request cannot be null");
//        }
        log.info("Registering new user");
        validateExistingEmail(request.getEmail());
        User newUser = createAndSaveUser(request);
        RegisterResponse response = modelMapper.map(newUser, RegisterResponse.class);
        response.setMessage("Successfully registered");
        log.error("user {}", newUser);
        log.info("User successfully registered with authorities: {}", newUser.getAuthorities());
        return new ApiResponse<>(LocalDateTime.now(), true, response);
    }

    @Override
    public void blacklist(String token) {
        token = mutateToken(token);
        log.info("Trying to blacklist token: {}", token);
        BlacklistedToken blacklistedToken = new BlacklistedToken();
        blacklistedToken.setToken(token);
        blacklistedToken.setExpiresAt(now().plus(24, HOURS));
        blacklistedTokenRepository.save(blacklistedToken);
        log.info("Blacklisted token: {}", token);
    }

    @Override
    public boolean isTokenBlacklisted(String token) {
        token = mutateToken(token);
        log.info("Checking blacklist status of token: {}", token);
        boolean isBlacklisted = blacklistedTokenRepository.existsByToken(token);
        log.info("Blacklist status of token: {}", isBlacklisted);
        return isBlacklisted;
    }

    @Override
    public User update(RegisterRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).map().orElseThrow(null);
        user.getAuthorities().add(request.getAuthority());
        return userRepository.save(user);
    }

    @Scheduled(cron = "1 0 * * *")
    private void deleteExpiredTokens() {
        log.info("Tracking and deleting expired user tokens");
        var blacklist = blacklistedTokenRepository.findAll();
        blacklist.stream()
                .filter(blacklistedToken -> now().isAfter(blacklistedToken.getExpiresAt()))
                .forEach(blacklistedTokenRepository::delete);
        log.info("Expired user tokens successfully tracked and deleted");
    }

    private static String mutateToken(String token) {
        int beginIndex = token.indexOf(".") + 1;
        int endIndex = token.lastIndexOf(".");
        return token.substring(beginIndex, endIndex);
    }

    private User createAndSaveUser(RegisterRequest request) {
        User newUser = modelMapper.map(request, User.class);
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
//        newUser.setAuthorities(new HashSet<>());
        newUser.setAuthorities(Set.of(request.getAuthority()));
        return userRepository.save(newUser);
    }

    private void validateExistingEmail(String email) {
        boolean emailExists = userRepository.existsByEmail(email);
//        if (emailExists) throw new EmailExistsException(email + " already exists");
    }

}
