package com.thekuzea.experimental.domain.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.thekuzea.experimental.api.dto.RoleDto;
import com.thekuzea.experimental.domain.model.Role;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RoleMapper {

    public static Role mapToModel(final RoleDto dto) {
        return Role.builder()
                .name(dto.getName())
                .build();
    }

    public static RoleDto mapToDto(final Role entity) {
        return RoleDto.builder()
                .id(entity.getId().toString())
                .name(entity.getName())
                .build();
    }
}
