package com.digitalmart.application.Auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.WebUtils;

import javax.security.auth.login.LoginException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest payload) throws LoginException {

        // authService.login must now return an object containing both tokens
        TokenPair tokens = authService.login(payload);

        if(tokens != null) {

            // 1. Build the Access Token Cookie (15 minutes)
            ResponseCookie accessCookie = ResponseCookie.from("access_token", tokens.accessToken())
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(15 * 60)
                    .sameSite("None")
                    .build();

            // 2. Build the Refresh Token Cookie (30 days, restricted path)
            ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", tokens.refreshToken())
                    .httpOnly(true)
                    .secure(true)
                    .path("/api/v1/auth/refresh")
                    .maxAge(30 * 24 * 60 * 60)
                    .sameSite("None")
                    .build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
                    .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                    .body("You are logged in successfully..");
        }

        throw new LoginException("Login Failed!");
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refreshSession(HttpServletRequest request) {

        // 1. Extract the Refresh Token from the restricted cookie
        Cookie refreshCookie = WebUtils.getCookie(request, "refresh_token");

        log.info("refresh_token {}",refreshCookie.getValue());

        if (refreshCookie.getValue().isEmpty()) {
            return ResponseEntity.status(401).body("Missing refresh token");
        }

        // 2. Pass to service layer to validate via JwtUtil and generate new tokens
        // If validation fails in the service, it should return null or throw an exception
        TokenPair newTokens = authService.refresh(refreshCookie.getValue());

        if (newTokens != null) {
            ResponseCookie accessCookie = ResponseCookie.from("access_token", newTokens.accessToken())
                    .httpOnly(true).secure(true).path("/").maxAge(15 * 60).sameSite("None").build();

            ResponseCookie newRefreshCookie = ResponseCookie.from("refresh_token", newTokens.refreshToken())
                    .httpOnly(true).secure(true).path("/api/v1/auth/").maxAge(30 * 24 * 60 * 60).sameSite("None").build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
                    .header(HttpHeaders.SET_COOKIE, newRefreshCookie.toString())
                    .body("Session refreshed successfully");
        }

        return ResponseEntity.status(401).body("Invalid or expired refresh token");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {

        // Overwrite both cookies with empty values and 0 expiration to clear them from the client
        ResponseCookie clearAccess = ResponseCookie.from("access_token", "")
                .httpOnly(true).secure(true).path("/").maxAge(0).sameSite("None").build();

        ResponseCookie clearRefresh = ResponseCookie.from("refresh_token", "")
                .httpOnly(true).secure(true).path("/api/v1/auth/").maxAge(0).sameSite("None").build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, clearAccess.toString())
                .header(HttpHeaders.SET_COOKIE, clearRefresh.toString())
                .body("Logged out successfully");
    }
}