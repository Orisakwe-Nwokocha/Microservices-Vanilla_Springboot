package dev.orisha.user_service.data.mappers.impls;

import dev.orisha.user_service.data.constants.Authority;
import dev.orisha.user_service.data.mappers.UserMapper;
import dev.orisha.user_service.data.models.User;
import dev.orisha.user_service.dto.UserDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDTO toDto(User entity) {
        if (entity == null) return null;

        UserDTO userDTO = new UserDTO();

        userDTO.setId(entity.getId());
        userDTO.setFirstName(entity.getFirstName());
        userDTO.setLastName(entity.getLastName());
        userDTO.setEmail(entity.getEmail());
        userDTO.setPassword(entity.getPassword());
        userDTO.setAuthorities(entity.getAuthorities());
        userDTO.setDateRegistered(entity.getDateRegistered());
        userDTO.setDateUpdated(entity.getDateUpdated());

        return userDTO;
    }

    @Override
    public User toEntity(UserDTO dto) {
        return null;
    }

    @Override
    public List<UserDTO> toDtoList(List<User> entityList) {
        return List.of();
    }

    @Override
    public List<User> toEntityList(List<UserDTO> dtoList) {
        return List.of();
    }
}
