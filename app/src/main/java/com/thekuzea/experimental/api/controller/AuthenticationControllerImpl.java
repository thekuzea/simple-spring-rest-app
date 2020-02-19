package com.thekuzea.experimental.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import com.thekuzea.experimental.api.dto.TokenDto;
import com.thekuzea.experimental.api.dto.UserDto;
import com.thekuzea.experimental.infrastructure.security.service.AuthenticationService;

@RestController
@RequiredArgsConstructor
public class AuthenticationControllerImpl implements AuthenticationController {

    private final AuthenticationService authenticationService;

    @Override
    public TokenDto authenticate(final UserDto userDto) {
        return authenticationService.authenticateUser(userDto);
    }
}
