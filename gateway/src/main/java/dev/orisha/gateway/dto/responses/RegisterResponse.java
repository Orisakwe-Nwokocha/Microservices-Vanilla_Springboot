package dev.orisha.gateway.dto.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterResponse {
    @JsonProperty("user_id")
    private Long id;
    private String email;
    private String message;
}
