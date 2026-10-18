package com.digitalmart.application.Auth;

public record TokenPair(
        String accessToken,
        String refreshToken
) {
}
