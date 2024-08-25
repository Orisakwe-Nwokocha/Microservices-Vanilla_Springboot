package dev.orisha.user_service.security.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.orisha.user_service.config.AppConfig;
import dev.orisha.user_service.dto.requests.LoginRequest;
import dev.orisha.user_service.dto.responses.ApiResponse;
import dev.orisha.user_service.dto.responses.ErrorResponse;
import dev.orisha.user_service.dto.responses.LoginResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.Collection;

import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.HOURS;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@AllArgsConstructor
@Slf4j
public class CustomUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper mapper = new ObjectMapper();
    private final AuthenticationManager authenticationManager;
    private final AppConfig appConfig;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
                                                                throws AuthenticationException {
        log.info("Starting user authentication");

        LoginRequest loginRequest;
        try(InputStream inputStream = request.getInputStream()) {
            loginRequest = mapper.readValue(inputStream, LoginRequest.class);
        } catch (IOException e) {
            throw new AuthenticationCredentialsNotFoundException(e.getMessage());
        }
        String username = loginRequest.getEmail().toLowerCase();
        String password = loginRequest.getPassword();

        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authResult = authenticationManager.authenticate(authentication);
        SecurityContextHolder.getContext().setAuthentication(authResult);
        log.info("Retrieved the authentication result from authentication manager");
        return authResult;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult)
                                                        throws IOException, ServletException {
        String token = generateAccessToken(authResult);
        Cookie cookie = createCookie(token);
        response.addCookie(cookie);

        LoginResponse loginResponse = buildLoginResponse(token);
        ApiResponse<LoginResponse> apiResponse = new ApiResponse<>(now(), true, loginResponse);
        response.setContentType(APPLICATION_JSON_VALUE);
        response.getOutputStream().write(mapper.writeValueAsBytes(apiResponse));
        response.flushBuffer();

        log.info("User authentication successful");
        chain.doFilter(request, response);
    }


    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException exception) throws IOException, ServletException {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .responseTime(now())
                .isSuccessful(false)
                .error("UnsuccessfulAuthentication")
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();

        response.setStatus(SC_UNAUTHORIZED);
        response.setContentType(APPLICATION_JSON_VALUE);
        response.getOutputStream().write(mapper.writeValueAsBytes(errorResponse));
        response.flushBuffer();
        log.info("User authentication unsuccessful");
    }

    private static LoginResponse buildLoginResponse(String token) {
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(token);
        loginResponse.setMessage("Authentication succeeded");
        return loginResponse;
    }

    private static Cookie createCookie(String token) {
        Cookie cookie = new Cookie("orisha.dev", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(86400);
        cookie.setSecure(true);
//        cookie.setDomain("http://localhost:3000");
        return cookie;
    }

    private String[] extractAuthorities(Collection<? extends GrantedAuthority> authorities) {
        return authorities
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toArray(String[]::new);
    }

    private String generateAccessToken(Authentication authResult) {
        Algorithm algorithm = Algorithm.HMAC512(appConfig.getSecretKey());
        Instant now = Instant.now();
        UserDetails user = (UserDetails) authResult.getPrincipal();
        return JWT.create()
                .withIssuer("orisha.dev")
                .withIssuedAt(now)
                .withExpiresAt(now.plus(24, HOURS))
                .withSubject(user.getUsername())
                .withArrayClaim("authorities", extractAuthorities(authResult.getAuthorities()))
                .sign(algorithm);
    }
}
