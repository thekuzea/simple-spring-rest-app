package com.thekuzea.experimental.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import com.thekuzea.experimental.api.dto.UserDto;
import com.thekuzea.experimental.domain.model.Role;
import com.thekuzea.experimental.domain.model.User;
import com.thekuzea.experimental.infrastructure.persistence.RoleRepository;
import com.thekuzea.experimental.infrastructure.persistence.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import static com.thekuzea.experimental.support.constant.messages.entity.RoleMessages.DEFAULT_ROLE_NOT_FOUND;
import static com.thekuzea.experimental.support.constant.messages.entity.RoleMessages.ROLE_NOT_FOUND;
import static com.thekuzea.experimental.support.constant.messages.entity.UserMessages.USERNAME_IS_ALREADY_USED;
import static com.thekuzea.experimental.support.constant.messages.entity.UserMessages.USER_ALREADY_EXISTS;
import static com.thekuzea.experimental.support.constant.messages.entity.UserMessages.USER_NOT_FOUND;
import static com.thekuzea.experimental.test.util.dto.UserDtoTestDataGenerator.createUserDto;
import static com.thekuzea.experimental.test.util.dto.UserDtoTestDataGenerator.createUserDtoForSave;
import static com.thekuzea.experimental.test.util.model.RoleModelTestDataGenerator.createUserRole;
import static com.thekuzea.experimental.test.util.model.UserModelTestDataGenerator.createUser;
import static com.thekuzea.experimental.test.util.model.UserModelTestDataGenerator.createUserList;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private static final String DEFAULT_ROLE = "user";

    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository, roleRepository, bCryptPasswordEncoder);
        ReflectionTestUtils.setField(userService, "defaultRole", DEFAULT_ROLE);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(userRepository, roleRepository, bCryptPasswordEncoder);
    }

    @Test
    void shouldGetAllUsers() {
        final List<User> userList = createUserList();
        when(userRepository.findAll()).thenReturn(userList);

        final List<UserDto> actualDtoList = userService.getAllUsers();

        verify(userRepository).findAll();
        assertThat(actualDtoList.size()).isEqualTo(3);
    }

    @Test
    void shouldNotGetAllUsers() {
        final List<User> userList = Collections.emptyList();
        when(userRepository.findAll()).thenReturn(userList);

        final List<UserDto> actualDtoList = userService.getAllUsers();

        verify(userRepository).findAll();
        assertThat(actualDtoList).isEmpty();
    }

    @Test
    void shouldGetUserByUsername() {
        final UserDto expectedUserDto = createUserDto();
        final User expectedUser = createUser();
        when(userRepository.findByUsername(expectedUser.getUsername())).thenReturn(Optional.of(expectedUser));

        final UserDto actualUserDto = userService.getByUsername(expectedUser.getUsername());

        verify(userRepository).findByUsername(expectedUser.getUsername());
        assertThat(actualUserDto).isEqualTo(expectedUserDto);
    }

    @Test
    void shouldNotGetUserByUsername() {
        final String username = "unknown";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getByUsername(username))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(USER_NOT_FOUND);

        verify(userRepository).findByUsername(username);
    }

    @Test
    void shouldAddNewUser() {
        final UserDto expectedUserDto = createUserDtoForSave();
        final User expectedUser = createUser();
        final Role role = createUserRole();
        final String username = expectedUserDto.getUsername();
        when(userRepository.existsByUsername(username)).thenReturn(false);
        when(roleRepository.findByName(DEFAULT_ROLE)).thenReturn(Optional.of(role));
        when(bCryptPasswordEncoder.encode(expectedUserDto.getPassword())).thenReturn(expectedUser.getPassword());

        final UserDto actualUserDto = userService.addNewUser(expectedUserDto);

        verify(userRepository).existsByUsername(username);
        verify(roleRepository).findByName(DEFAULT_ROLE);
        verify(bCryptPasswordEncoder).encode(expectedUserDto.getPassword());
        verify(userRepository).save(refEq(expectedUser, "id"));
        assertThat(actualUserDto).isEqualTo(expectedUserDto);
    }

    @Test
    void shouldNotAddNewUser() {
        final UserDto expectedUserDto = createUserDto();
        final User expectedUser = createUser();
        final Role role = createUserRole();
        when(userRepository.existsByUsername(expectedUser.getUsername())).thenReturn(true);
        when(roleRepository.findByName(DEFAULT_ROLE)).thenReturn(Optional.of(role));

        assertThatThrownBy(() -> userService.addNewUser(expectedUserDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(USER_ALREADY_EXISTS);

        verify(userRepository).existsByUsername(expectedUser.getUsername());
        verify(roleRepository).findByName(DEFAULT_ROLE);
    }

    @Test
    void shouldNotAddNewUserRoleNotFound() {
        final UserDto expectedUserDto = createUserDto();
        final User expectedUser = createUser();
        when(userRepository.existsByUsername(expectedUser.getUsername())).thenReturn(false);
        when(roleRepository.findByName(DEFAULT_ROLE)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.addNewUser(expectedUserDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(DEFAULT_ROLE_NOT_FOUND);

        verify(userRepository).existsByUsername(expectedUser.getUsername());
        verify(roleRepository).findByName(DEFAULT_ROLE);
    }

    @Test
    void shouldUpdateUserByUserId() {
        final String id = "3f9198d8-6066-4438-a3bf-fd8cf8d33925";
        final UserDto userDtoForUpdate = createUserDtoForSave();
        final UserDto expectedUserDto = createUserDto();
        final User user = createUser();
        final Role role = createUserRole();
        when(userRepository.findById(UUID.fromString(id))).thenReturn(Optional.of(user));
        when(userRepository.findByUsername(userDtoForUpdate.getUsername())).thenReturn(Optional.empty());
        when(roleRepository.findByName(DEFAULT_ROLE)).thenReturn(Optional.of(role));
        when(bCryptPasswordEncoder.encode(userDtoForUpdate.getPassword())).thenReturn(user.getPassword());

        final UserDto actualUserDto = userService.updateByUserId(id, userDtoForUpdate);

        verify(userRepository).findById(UUID.fromString(id));
        verify(userRepository).findByUsername(userDtoForUpdate.getUsername());
        verify(bCryptPasswordEncoder).encode(userDtoForUpdate.getPassword());
        verify(roleRepository).findByName(DEFAULT_ROLE);
        verify(userRepository).save(user);
        assertThat(actualUserDto).isEqualTo(expectedUserDto);
    }

    @Test
    void shouldNotUpdateUserByUserIdReasonRoleNotFound() {
        final String id = "3f9198d8-6066-4438-a3bf-fd8cf8d33925";
        final UserDto userDto = createUserDto();
        final User user = createUser();
        when(userRepository.findById(UUID.fromString(id))).thenReturn(Optional.of(user));
        when(userRepository.findByUsername(userDto.getUsername())).thenReturn(Optional.empty());
        when(roleRepository.findByName(DEFAULT_ROLE)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateByUserId(id, userDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ROLE_NOT_FOUND);

        verify(userRepository).findById(UUID.fromString(id));
        verify(userRepository).findByUsername(userDto.getUsername());
        verify(roleRepository).findByName(DEFAULT_ROLE);
    }

    @Test
    void shouldNotUpdateUserByUserIdReasonUserNotFound() {
        final String id = "3f9198d8-6066-4438-a3bf-fd8cf8d33925";
        final UserDto userDto = createUserDto();
        when(userRepository.findById(UUID.fromString(id))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateByUserId(id, userDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(USER_NOT_FOUND);

        verify(userRepository).findById(UUID.fromString(id));
    }

    @Test
    void shouldNotUpdateUserByUserIdReasonUsernameIsAlreadyUsed() {
        final String id = "3f9198d8-6066-4438-a3bf-fd8cf8d33925";
        final UserDto userDto = createUserDto();
        final User user = createUser();
        when(userRepository.findById(UUID.fromString(id))).thenReturn(Optional.of(user));
        when(userRepository.findByUsername(userDto.getUsername())).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> userService.updateByUserId(id, userDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(USERNAME_IS_ALREADY_USED);

        verify(userRepository).findById(UUID.fromString(id));
        verify(userRepository).findByUsername(userDto.getUsername());
    }

    @Test
    void shouldDeleteUserByUsername() {
        final User expectedUser = createUser();
        final String username = expectedUser.getUsername();
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(expectedUser));

        userService.deleteByUsername(username);

        verify(userRepository).findByUsername(username);
        verify(userRepository).deleteByUsername(username);
    }

    @Test
    void shouldNotDeleteUserByUsername() {
        final String username = "unknown";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.deleteByUsername(username))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(USER_NOT_FOUND);

        verify(userRepository).findByUsername(username);
    }
}
