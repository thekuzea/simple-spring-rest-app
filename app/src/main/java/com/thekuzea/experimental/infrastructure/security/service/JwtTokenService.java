package com.thekuzea.experimental.infrastructure.security.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtTokenService {

    String getUsernameFromToken(String token);

    String generateToken(UserDetails userDetails);

    boolean validateToken(String token, UserDetails userDetails);
}
