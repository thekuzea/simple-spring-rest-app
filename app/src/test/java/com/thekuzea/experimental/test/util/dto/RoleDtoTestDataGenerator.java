package com.thekuzea.experimental.test.util.dto;

import java.util.Arrays;
import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.thekuzea.experimental.api.dto.RoleDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RoleDtoTestDataGenerator {

    public static RoleDto createRoleDto() {
        return RoleDto.builder()
                .id("4d1f13aa-7065-11ea-bc55-0242ac130003")
                .name("user")
                .build();
    }

    public static RoleDto createRoleDtoAsNewRole() {
        return RoleDto.builder()
                .name("moderator")
                .build();
    }

    public static RoleDto createRoleDtoAsNewRoleResponse() {
        return RoleDto.builder()
                .id("4b8c17d6-f509-4569-8502-e7c2b0d8f13c")
                .name("moderator")
                .build();
    }

    public static List<RoleDto> createRoleDtoList() {
        final RoleDto role1 = RoleDto.builder()
                .id("65e65650-7065-11ea-bc55-0242ac130003")
                .name("admin")
                .build();

        final RoleDto role2 = RoleDto.builder()
                .id("4d1f13aa-7065-11ea-bc55-0242ac130003")
                .name("user")
                .build();

        return Arrays.asList(role1, role2);
    }

    public static RoleDto createRoleDtoNegativeFlow() {
        return RoleDto.builder()
                .name("user")
                .build();
    }
}
