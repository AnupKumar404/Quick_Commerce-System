package com.digitalmart.application.UserIdentity;

import java.math.BigInteger;

public record UserResponseDto(
        String firstname,
        String lastname,
        String email,
        BigInteger phoneNumber
) {
}
