package com.thekuzea.experimental.domain.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.thekuzea.experimental.api.dto.UserDto;
import com.thekuzea.experimental.domain.model.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserMapper {

    public static User mapToModel(final UserDto dto) {
        return User.builder()
                .username(dto.getUsername())
                .password(dto.getPassword())
                .build();
    }

    public static UserDto mapToDto(final User entity) {
        return UserDto.builder()
                .id(entity.getId().toString())
                .username(entity.getUsername())
                .role(entity.getRole().getName())
                .build();
    }
}
