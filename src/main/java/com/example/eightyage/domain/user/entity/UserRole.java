package com.example.eightyage.domain.user.entity;

import com.example.eightyage.global.exception.UnauthorizedException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum UserRole {

    ROLE_USER(Authority.USER),
    ROLE_ADMIN(Authority.ADMIN);

    private final String userRole;

    public static UserRole of(String role) {
        return Arrays.stream(UserRole.values())
                .filter(r -> r.getUserRole().equalsIgnoreCase(role))
                .findFirst()
                .orElseThrow(() -> new UnauthorizedException("유효하지 않은 UserRole"));
    }

    public static class Authority {
        public static final String USER = "ROLE_USER";
        public static final String ADMIN = "ROLE_ADMIN";
    }
}
