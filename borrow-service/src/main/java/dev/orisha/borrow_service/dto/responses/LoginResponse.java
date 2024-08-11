package dev.orisha.borrow_service.dto.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private String message;
    private String token;
}
