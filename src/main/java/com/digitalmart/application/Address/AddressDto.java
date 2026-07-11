package com.digitalmart.application.Address;

public record AddressDto(
        Long id,
        String addressLine1,
        String addressLine2,
        String city,
        String state,
        String pinCode,
        String type
) {
}
