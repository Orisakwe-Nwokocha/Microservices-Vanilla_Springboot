package dev.orisha.borrow_service.services.utils.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static dev.orisha.borrow_service.security.utils.SecurityUtils.JWT_PREFIX;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Configuration
public class FeignClientConfiguration {


    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            String token = getAuthToken();
            requestTemplate.header(AUTHORIZATION, token);
        };
    }

    private String getAuthToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getCredentials() instanceof String credentials) {
            return JWT_PREFIX + credentials;
        }
        throw new IllegalStateException("Please login to perform this action");
    }
}
