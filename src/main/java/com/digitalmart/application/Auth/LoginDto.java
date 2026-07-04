package com.digitalmart.application.Auth;

import com.digitalmart.application.UserIdentity.UserRole;

public record LoginDto(
        Long id,
        String email,
        String passwordHash,
        UserRole role
) {
}
