package com.digitalmart.application.UserIdentity;

import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDto> register(@RequestBody UserDto dto) throws DuplicateRequestException {
        return new ResponseEntity<>(userService.register(dto), HttpStatus.CREATED);
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileDto> register(@RequestParam("id") Long id) throws DuplicateRequestException {
        return new ResponseEntity<>(userService.getUserProfile(id), HttpStatus.OK);
    }
}
