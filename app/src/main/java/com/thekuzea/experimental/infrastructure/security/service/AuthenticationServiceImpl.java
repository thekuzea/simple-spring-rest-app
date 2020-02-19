package com.thekuzea.experimental.infrastructure.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.thekuzea.experimental.api.dto.TokenDto;
import com.thekuzea.experimental.api.dto.UserDto;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;

    private final UserDetailsService jwtUserDetailsService;

    private final JwtTokenService jwtTokenService;

    @Override
    public TokenDto authenticateUser(final UserDto userDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword())
        );

        final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(userDto.getUsername());
        final String token = jwtTokenService.generateToken(userDetails);

        return new TokenDto(token);
    }
}
