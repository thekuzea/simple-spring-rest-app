package com.thekuzea.experimental.infrastructure.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GlobalWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private final UserDetailsService jwtUserDetailsService;

    private final JwtRequestFilter jwtRequestFilter;

    @Autowired
    public void configureGlobal(final AuthenticationManagerBuilder authenticationManagerBuilder,
                                final PasswordEncoder passwordEncoder) throws Exception {

        authenticationManagerBuilder.userDetailsService(jwtUserDetailsService)
                .passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(final HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf()
                .disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/generate-random-data/**")
                .permitAll()
                .antMatchers(HttpMethod.POST, "/auth", "/user")
                .permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
