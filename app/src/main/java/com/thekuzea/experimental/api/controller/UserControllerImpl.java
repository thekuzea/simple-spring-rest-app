package com.thekuzea.experimental.api.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import com.thekuzea.experimental.api.dto.UserDto;
import com.thekuzea.experimental.service.UserService;

@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

    private final UserService userService;

    @Override
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @Override
    public UserDto getByUsername(final String username) {
        return userService.getByUsername(username);
    }

    @Override
    public UserDto addNewUser(final UserDto dto) {
        return userService.addNewUser(dto);
    }

    @Override
    public UserDto updateByUserId(final String userId, final UserDto dto) {
        return userService.updateByUserId(userId, dto);
    }

    @Override
    public void deleteByUsername(final String username) {
        userService.deleteByUsername(username);
    }
}
