package dev.orisha.gateway.security.data.repositories;

import dev.orisha.gateway.security.data.models.BlacklistedToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlacklistedTokenRepository extends JpaRepository<BlacklistedToken, Long> {
    boolean existsByToken(String token);
}
