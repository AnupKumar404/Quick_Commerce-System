package com.digitalmart.application.UserIdentity;

import java.math.BigInteger;

public record UserDto(
        String firstname,
        String lastname,
        String email,
        String passwordHash,
        BigInteger phoneNumber
) {
}
