package dev.orisha.user_service.dto.requests;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dev.orisha.user_service.data.constants.Authority;
import dev.orisha.user_service.dto.UserDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Set;

@Setter
@Getter
@ToString
@JsonIgnoreProperties({"password", "authorities", "dateRegistered", "dateUpdated"})

public class UserUpdateRequest extends UserDTO {

    private Authority authority;

    @JsonIgnore
    private Long id;

    private String password;

    private Set<Authority> authorities;

    private LocalDateTime dateRegistered;

    private LocalDateTime dateUpdated;

}
