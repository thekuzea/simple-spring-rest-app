package com.thekuzea.experimental.test.util.dto;

import java.util.Arrays;
import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.thekuzea.experimental.api.dto.UserDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserDtoTestDataGenerator {

    public static UserDto createUserDto() {
        return UserDto.builder()
                .id("762eb61a-7065-11ea-bc55-0242ac130003")
                .username("Larry")
                .role("user")
                .build();
    }

    public static UserDto createUserDtoForSave() {
        return UserDto.builder()
                .id("762eb61a-7065-11ea-bc55-0242ac130003")
                .username("Larry")
                .password("password123")
                .role("user")
                .build();
    }

    public static UserDto createUserDtoAsUpdateUserResponse() {
        return UserDto.builder()
                .id("afa06be6-7065-11ea-bc55-0242ac130003")
                .username("Kate")
                .role("user")
                .build();
    }

    public static UserDto createUserDtoForMapper() {
        return UserDto.builder()
                .id("762eb61a-7065-11ea-bc55-0242ac130003")
                .username("Larry")
                .password("password123")
                .build();
    }

    public static UserDto createUserDtoAsNewUser() {
        return UserDto.builder()
                .username("Nataly")
                .password("password123")
                .build();
    }

    public static UserDto createUserDtoAsNewUserResponse() {
        return UserDto.builder()
                .username("Nataly")
                .role("user")
                .build();
    }

    public static List<UserDto> createUserDtoList() {
        final UserDto user1 = UserDto.builder()
                .id("84db923c-7065-11ea-bc55-0242ac130003")
                .username("root")
                .role("admin")
                .build();

        final UserDto user2 = UserDto.builder()
                .id("762eb61a-7065-11ea-bc55-0242ac130003")
                .username("Larry")
                .role("user")
                .build();

        final UserDto user3 = UserDto.builder()
                .id("afa06be6-7065-11ea-bc55-0242ac130003")
                .username("Kate")
                .role("user")
                .build();

        return Arrays.asList(user1, user2, user3);
    }

    public static UserDto createUserDtoNegativeFlowUserAlreadyExists() {
        return UserDto.builder()
                .username("Larry")
                .password("password123")
                .build();
    }

    public static UserDto createUserDtoNegativeFlowDefaultRoleNotFound() {
        return UserDto.builder()
                .username("Julia")
                .password("password123")
                .build();
    }
}
