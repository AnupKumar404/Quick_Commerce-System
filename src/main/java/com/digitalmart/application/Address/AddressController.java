package com.digitalmart.application.Address;

import com.digitalmart.application.Utils.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<String> addAddress(
            @RequestBody AddressDto addressDto,
            @AuthenticationPrincipal UserPrincipal currentUser
            ) throws BadRequestException {

        Long userId = currentUser.getUserId();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(addressService.addAddress(userId, addressDto));
    }
}
