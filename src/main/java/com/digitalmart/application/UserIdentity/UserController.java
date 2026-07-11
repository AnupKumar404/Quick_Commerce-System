package com.digitalmart.application.UserIdentity;

import com.digitalmart.application.Utils.UserPrincipal;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<String> register(@RequestBody UserDao dto) throws DuplicateRequestException, BadRequestException {
        return new ResponseEntity<>(userService.register(dto), HttpStatus.CREATED);
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileDto> getUserProfile(@AuthenticationPrincipal UserPrincipal currentUser) throws NoSuchElementException {
        Long userId = currentUser.getUserId();
        return new ResponseEntity<>(userService.getUserProfile(userId), HttpStatus.OK);
    }
}
