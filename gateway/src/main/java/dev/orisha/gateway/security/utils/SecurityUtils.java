package dev.orisha.gateway.security.utils;

import java.util.List;

public class SecurityUtils {

    private SecurityUtils() {}

    public static final String JWT_PREFIX = "Bearer ";

    public static final List<String> PUBLIC_ENDPOINTS = List.of(
                "/api/v1/auth/register",
                "/api/v1/auth/login",
                "/api/v1/auth/logout",
                "/api/v1/auth/**"
    );

    public static final String[] ADMIN_AUTH_ENDPOINTS = {

    };

    public static final String[] USER_AUTH_ENDPOINTS = {

    };

}
