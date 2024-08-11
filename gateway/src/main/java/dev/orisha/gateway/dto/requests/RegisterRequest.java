package dev.orisha.gateway.dto.requests;

import dev.orisha.gateway.data.constants.Authority;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Authority authority;
}

