package com.digitalmart.application.Address;

public record AddressDto(
        String addressLine1,
        String addressLine2,
        String city,
        String state,
        String pinCode,
        String type
) {
}
