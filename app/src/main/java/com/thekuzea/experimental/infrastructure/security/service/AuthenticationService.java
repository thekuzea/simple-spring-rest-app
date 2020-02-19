package com.thekuzea.experimental.infrastructure.security.service;

import com.thekuzea.experimental.api.dto.TokenDto;
import com.thekuzea.experimental.api.dto.UserDto;

public interface AuthenticationService {

    TokenDto authenticateUser(UserDto userDto);
}
