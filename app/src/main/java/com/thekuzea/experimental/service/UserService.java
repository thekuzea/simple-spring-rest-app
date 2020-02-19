package com.thekuzea.experimental.service;

import java.util.List;

import com.thekuzea.experimental.api.dto.UserDto;

public interface UserService {

    List<UserDto> getAllUsers();

    UserDto getByUsername(String username);

    UserDto addNewUser(UserDto dto);

    UserDto updateByUserId(String userId, UserDto dto);

    void deleteByUsername(String username);
}
