package dev.orisha.user_service.security.services;

import dev.orisha.user_service.data.models.User;
import dev.orisha.user_service.data.repositories.UserRepository;
import dev.orisha.user_service.security.data.models.SecureUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Loading user: {}", username);
        User user = userRepository.findByEmail(username)
                .orElseThrow(()-> new UsernameNotFoundException("Invalid username or password"));
        log.info("User found with email: {}", user.getEmail());
        return new SecureUser(user);
    }
}
