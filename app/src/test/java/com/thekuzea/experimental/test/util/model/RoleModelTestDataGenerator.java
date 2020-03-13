package com.thekuzea.experimental.test.util.model;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.thekuzea.experimental.domain.model.Role;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RoleModelTestDataGenerator {

    public static Role createUserRole() {
        return Role.builder()
                .id(UUID.fromString("4d1f13aa-7065-11ea-bc55-0242ac130003"))
                .name("user")
                .build();
    }

    public static Role createAdminRole() {
        return Role.builder()
                .id(UUID.fromString("65e65650-7065-11ea-bc55-0242ac130003"))
                .name("admin")
                .build();
    }

    public static List<Role> createRoleList() {
        final Role role1 = Role.builder()
                .id(UUID.fromString("65e65650-7065-11ea-bc55-0242ac130003"))
                .name("admin")
                .build();

        final Role role2 = Role.builder()
                .id(UUID.fromString("4d1f13aa-7065-11ea-bc55-0242ac130003"))
                .name("user")
                .build();

        return Arrays.asList(role1, role2);
    }
}
