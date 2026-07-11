package com.digitalmart.application.UserIdentity;

import java.math.BigInteger;

public record UserDao(
        String firstname,
        String lastname,
        String email,
        String passwordHash,
        String gender,
        BigInteger phoneNumber
) {
}
