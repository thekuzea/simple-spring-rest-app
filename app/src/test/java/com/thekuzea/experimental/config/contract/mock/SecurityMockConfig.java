package com.thekuzea.experimental.config.contract.mock;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.thekuzea.experimental.infrastructure.security.service.JwtTokenService;
import com.thekuzea.experimental.test.util.AuthenticationTestDataGenerator;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static com.thekuzea.experimental.config.contract.mock.SecurityMockConstants.ROOT_USERNAME;
import static com.thekuzea.experimental.config.contract.mock.SecurityMockConstants.TOKEN;

@TestConfiguration
@Profile("contract")
public class SecurityMockConfig {

    @Bean(name = "authenticationManagerBean")
    @Primary
    public AuthenticationManager authenticationManager() {
        final AuthenticationManager authenticationManager = mock(AuthenticationManager.class);

        final Authentication unsuccessfulAuthentication = AuthenticationTestDataGenerator.createUnsuccessfulAuthentication();
        when(authenticationManager.authenticate(refEq(unsuccessfulAuthentication)))
                .thenThrow(new BadCredentialsException(StringUtils.EMPTY));

        return authenticationManager;
    }

    @Bean
    @Primary
    public UserDetailsService jwtUserDetailsService() {
        final UserDetailsService jwtUserDetailsService = mock(UserDetailsService.class);

        when(jwtUserDetailsService.loadUserByUsername(ROOT_USERNAME)).thenReturn(AuthenticationTestDataGenerator.createUserDetailsForRootUser());

        return jwtUserDetailsService;
    }

    @Bean
    @Primary
    public JwtTokenService jwtTokenService() {
        final JwtTokenService jwtTokenService = mock(JwtTokenService.class);

        when(jwtTokenService.generateToken(any(UserDetails.class))).thenReturn(TOKEN);
        when(jwtTokenService.getUsernameFromToken(TOKEN)).thenReturn(ROOT_USERNAME);
        when(jwtTokenService.validateToken(eq(TOKEN), any(UserDetails.class))).thenReturn(true);

        return jwtTokenService;
    }
}
