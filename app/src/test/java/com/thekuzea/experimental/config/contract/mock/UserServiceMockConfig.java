package com.thekuzea.experimental.config.contract.mock;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import com.thekuzea.experimental.api.dto.UserDto;
import com.thekuzea.experimental.service.UserService;
import com.thekuzea.experimental.support.constant.messages.entity.UserMessages;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static com.thekuzea.experimental.support.constant.messages.entity.RoleMessages.DEFAULT_ROLE_NOT_FOUND;
import static com.thekuzea.experimental.support.constant.messages.entity.RoleMessages.ROLE_NOT_FOUND;
import static com.thekuzea.experimental.test.util.dto.UserDtoTestDataGenerator.createUserDto;
import static com.thekuzea.experimental.test.util.dto.UserDtoTestDataGenerator.createUserDtoAsNewUser;
import static com.thekuzea.experimental.test.util.dto.UserDtoTestDataGenerator.createUserDtoAsNewUserResponse;
import static com.thekuzea.experimental.test.util.dto.UserDtoTestDataGenerator.createUserDtoAsUpdateUserResponse;
import static com.thekuzea.experimental.test.util.dto.UserDtoTestDataGenerator.createUserDtoList;
import static com.thekuzea.experimental.test.util.dto.UserDtoTestDataGenerator.createUserDtoNegativeFlowDefaultRoleNotFound;
import static com.thekuzea.experimental.test.util.dto.UserDtoTestDataGenerator.createUserDtoNegativeFlowUserAlreadyExists;

@TestConfiguration
@Profile("contract")
public class UserServiceMockConfig {

    @Bean
    @Primary
    public UserService userService() {
        final UserService userService = mock(UserService.class);

        when(userService.getAllUsers()).thenReturn(createUserDtoList());
        when(userService.getByUsername("Larry")).thenReturn(createUserDto());
        when(userService.getByUsername("Alice")).thenThrow(new IllegalArgumentException(UserMessages.USER_NOT_FOUND));
        doThrow(new IllegalArgumentException(UserMessages.USER_NOT_FOUND)).when(userService).deleteByUsername("Alice");

        when(userService.addNewUser(refEq(createUserDtoAsNewUser())))
                .thenReturn(createUserDtoAsNewUserResponse());
        when(userService.addNewUser(refEq(createUserDtoNegativeFlowUserAlreadyExists())))
                .thenThrow(new IllegalArgumentException(UserMessages.USER_ALREADY_EXISTS));
        when(userService.addNewUser(refEq(createUserDtoNegativeFlowDefaultRoleNotFound())))
                .thenThrow(new IllegalArgumentException(DEFAULT_ROLE_NOT_FOUND));

        when(userService.updateByUserId(eq("afa06be6-7065-11ea-bc55-0242ac130003"), any(UserDto.class)))
                .thenReturn(createUserDtoAsUpdateUserResponse());
        when(userService.updateByUserId(eq("762eb61a-7065-11ea-bc55-0242ac130003"), any(UserDto.class)))
                .thenThrow(new IllegalArgumentException(ROLE_NOT_FOUND));
        when(userService.updateByUserId(eq("e158aaa4-7065-11ea-bc55-0242ac130003"), any(UserDto.class)))
                .thenThrow(new IllegalArgumentException(UserMessages.USER_NOT_FOUND));
        when(userService.updateByUserId(eq("84db923c-7065-11ea-bc55-0242ac130003"), any(UserDto.class)))
                .thenThrow(new IllegalArgumentException(UserMessages.USERNAME_IS_ALREADY_USED));

        return userService;
    }
}
