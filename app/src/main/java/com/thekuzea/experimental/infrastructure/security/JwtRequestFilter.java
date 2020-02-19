package com.thekuzea.experimental.infrastructure.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.thekuzea.experimental.support.constant.messages.logging.LoggingMessages;
import com.thekuzea.experimental.infrastructure.security.service.JwtTokenService;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    public static final String BEARER = "Bearer ";

    private static final String AUTHORIZATION = "Authorization";

    private final UserDetailsService jwtUserDetailsService;

    private final JwtTokenService jwtTokenService;

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain chain) throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader(AUTHORIZATION);
        String username = null;
        String jwtToken = null;

        if (requestTokenHeader != null && requestTokenHeader.startsWith(BEARER)) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtTokenService.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                log.error(LoggingMessages.UNABLE_TO_GET_TOKEN);
            } catch (ExpiredJwtException e) {
                log.debug(LoggingMessages.TOKEN_HAS_EXPIRED);
            }
        } else {
            log.debug(LoggingMessages.TOKEN_FORMAT_WARNING);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            final UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);

            if (jwtTokenService.validateToken(jwtToken, userDetails)) {
                final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        chain.doFilter(request, response);
    }
}
