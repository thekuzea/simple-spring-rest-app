package com.thekuzea.experimental.test.util.model;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.thekuzea.experimental.domain.model.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserModelTestDataGenerator {

    public static User createUser() {
        return User.builder()
                .id(UUID.fromString("762eb61a-7065-11ea-bc55-0242ac130003"))
                .username("Larry")
                .password("password123")
                .role(RoleModelTestDataGenerator.createUserRole())
                .build();
    }

    public static List<User> createUserList() {
        final User user1 = User.builder()
                .id(UUID.fromString("84db923c-7065-11ea-bc55-0242ac130003"))
                .username("root")
                .password("root")
                .role(RoleModelTestDataGenerator.createAdminRole())
                .build();

        final User user2 = User.builder()
                .id(UUID.fromString("762eb61a-7065-11ea-bc55-0242ac130003"))
                .username("Larry")
                .password("password123")
                .role(RoleModelTestDataGenerator.createUserRole())
                .build();

        final User user3 = User.builder()
                .id(UUID.fromString("afa06be6-7065-11ea-bc55-0242ac130003"))
                .username("Kate")
                .password("password456")
                .role(RoleModelTestDataGenerator.createUserRole())
                .build();

        return Arrays.asList(user1, user2, user3);
    }
}
