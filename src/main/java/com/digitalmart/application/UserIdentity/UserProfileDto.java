package com.digitalmart.application.UserIdentity;

import com.digitalmart.application.Address.AddressDto;

import java.math.BigInteger;
import java.util.Set;

public record UserProfileDto(
        String firstname,
        String lastname,
        String email,
        String gender,
        BigInteger phoneNumber,
        Set<AddressDto> addresses
)
{ }

