package com.thekuzea.experimental.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thekuzea.experimental.api.dto.UserDto;
import com.thekuzea.experimental.domain.mapper.UserMapper;
import com.thekuzea.experimental.domain.model.Role;
import com.thekuzea.experimental.domain.model.User;
import com.thekuzea.experimental.infrastructure.persistence.RoleRepository;
import com.thekuzea.experimental.infrastructure.persistence.UserRepository;
import com.thekuzea.experimental.support.constant.messages.entity.UserMessages;
import com.thekuzea.experimental.support.constant.messages.logging.LoggingMessages;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import static com.thekuzea.experimental.support.constant.messages.entity.RoleMessages.DEFAULT_ROLE_NOT_FOUND;
import static com.thekuzea.experimental.support.constant.messages.entity.RoleMessages.ROLE_NOT_FOUND;
import static com.thekuzea.experimental.support.constant.messages.logging.LoggingMessages.ROLE_NOT_FOUND_BY_NAME;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Value("${experimental.security.settings.default-role}")
    private String defaultRole;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getByUsername(final String username) {
        final Optional<User> possibleUser = userRepository.findByUsername(username);

        if (possibleUser.isPresent()) {
            return UserMapper.mapToDto(possibleUser.get());
        } else {
            log.debug(LoggingMessages.USER_NOT_FOUND_BY_USERNAME, username);
            throw new IllegalArgumentException(UserMessages.USER_NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public UserDto addNewUser(final UserDto dto) {
        final boolean userExists = userRepository.existsByUsername(dto.getUsername());
        final Optional<Role> foundRole = roleRepository.findByName(defaultRole);

        if (!userExists) {
            if (foundRole.isPresent()) {
                final User mappedUser = UserMapper.mapToModel(dto);
                mappedUser.setPassword(bCryptPasswordEncoder.encode(mappedUser.getPassword()));
                mappedUser.setRole(foundRole.get());

                userRepository.save(mappedUser);
                log.debug(LoggingMessages.SAVED_NEW_USER, mappedUser.getId());
            } else {
                log.error(ROLE_NOT_FOUND_BY_NAME, defaultRole);
                throw new IllegalArgumentException(DEFAULT_ROLE_NOT_FOUND);
            }
        } else {
            log.debug(LoggingMessages.USER_ALREADY_EXISTS_LOG, dto.getUsername());
            throw new IllegalArgumentException(UserMessages.USER_ALREADY_EXISTS);
        }

        return dto;
    }

    @Override
    @Transactional
    public UserDto updateByUserId(final String userId, final UserDto dto) {
        final UUID id = UUID.fromString(userId);
        final Optional<User> possibleUser = userRepository.findById(id);

        if (possibleUser.isPresent()) {
            final User user = possibleUser.get();
            modifyUser(user, dto);

            userRepository.save(user);
            log.debug(LoggingMessages.UPDATED_USER, userId);

            return UserMapper.mapToDto(possibleUser.get());
        } else {
            log.debug(LoggingMessages.USER_NOT_FOUND_BY_ID, userId);
            throw new IllegalArgumentException(UserMessages.USER_NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public void deleteByUsername(final String username) {
        final Optional<User> possibleUser = userRepository.findByUsername(username);

        if (possibleUser.isPresent()) {
            userRepository.deleteByUsername(username);
            log.debug(LoggingMessages.DELETED_USER, username);
        } else {
            log.debug(LoggingMessages.USER_NOT_FOUND_BY_USERNAME, username);
            throw new IllegalArgumentException(UserMessages.USER_NOT_FOUND);
        }
    }

    private void modifyUser(final User user, final UserDto userDto) {
        final String username = userDto.getUsername();
        if (isNotBlank(username)) {
            final Optional<User> possibleUserByUsername = userRepository.findByUsername(username);

            if (possibleUserByUsername.isPresent()) {
                throw new IllegalArgumentException(UserMessages.USERNAME_IS_ALREADY_USED);
            }
            user.setUsername(username);
        }

        final String password = userDto.getPassword();
        if (isNotBlank(password)) {
            user.setPassword(bCryptPasswordEncoder.encode(password));
        }

        final String role = userDto.getRole();
        if (isNotBlank(role)) {
            final Optional<Role> possibleRole = roleRepository.findByName(role);

            if (possibleRole.isPresent()) {
                user.setRole(possibleRole.get());
            } else {
                log.error(ROLE_NOT_FOUND_BY_NAME, role);
                throw new IllegalArgumentException(ROLE_NOT_FOUND);
            }
        }
    }
}
