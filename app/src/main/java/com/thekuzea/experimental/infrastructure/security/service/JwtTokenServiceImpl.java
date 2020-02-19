package com.thekuzea.experimental.infrastructure.security.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.thekuzea.experimental.infrastructure.security.JwtRequestFilter;

import static com.thekuzea.experimental.support.util.DateTimeUtils.convertDateToLocalDateTime;
import static com.thekuzea.experimental.support.util.DateTimeUtils.convertLocalDateTimeToDate;

@Service
@RequiredArgsConstructor
public class JwtTokenServiceImpl implements JwtTokenService {

    @Value("${experimental.security.settings.jwt.validity}")
    private Integer jwtTokenValidityHoursAmount;

    @Value("${experimental.security.settings.jwt.secret}")
    private String secret;

    @Override
    public String getUsernameFromToken(final String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    @Override
    public String generateToken(final UserDetails userDetails) {
        final Map<String, Object> claims = new HashMap<>();

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(convertLocalDateTimeToDate(LocalDateTime.now()))
                .setExpiration(convertLocalDateTimeToDate(LocalDateTime.now().plusHours(jwtTokenValidityHoursAmount)))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    @Override
    public boolean validateToken(final String token, final UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private Claims getAllClaimsFromToken(final String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token.replace(JwtRequestFilter.BEARER, StringUtils.EMPTY))
                .getBody();
    }

    private <T> T getClaimFromToken(final String token, final Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private boolean isTokenExpired(final String token) {
        final LocalDateTime expiration = getExpirationDateFromToken(token);
        return expiration.isBefore(LocalDateTime.now());
    }

    private LocalDateTime getExpirationDateFromToken(final String token) {
        return convertDateToLocalDateTime(getClaimFromToken(token, Claims::getExpiration));
    }
}
