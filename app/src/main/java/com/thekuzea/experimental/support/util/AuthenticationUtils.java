package com.thekuzea.experimental.support.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AuthenticationUtils {

    public static UserDetails getCurrentLoggedInUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserDetails) authentication.getPrincipal();
    }
}
