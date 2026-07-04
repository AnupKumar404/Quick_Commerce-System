package com.digitalmart.application.Auth;

public record LoginRequest(
        String email,
        String passwordHash
) {
}
