package com.digitalmart.application.Auth;

import com.digitalmart.application.Exceptions.CustomAuthEntryPoint;
import com.digitalmart.application.Utils.JwtUtil;
import com.digitalmart.application.Utils.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    // Inject UserDetailsService to load the user during the refresh flow
    private final UserDetailsService userDetailsService;

    // Logic of Login
    public TokenPair login(LoginRequest req) throws AuthenticationException {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.email(), req.passwordHash())
        );

        UserPrincipal user = (UserPrincipal) authentication.getPrincipal();

        if (user != null) {
            return new TokenPair(
                    jwtUtil.generateToken(user), // Access Token
                    jwtUtil.generateRefreshToken(user) // Refresh Token
            );
        }

        return null;
    }

    // Logic of Refreshing the Session
    public TokenPair refresh(String refreshToken) {

        // 1. Cryptographically validate the refresh token and check the token_type claim
        if (jwtUtil.checkExpiryOfToken(refreshToken) && jwtUtil.validateRefreshToken(refreshToken)) {

            // 2. Extract the user's email/username from the valid token
            String username = jwtUtil.extractUsername(refreshToken);

            if (username != null) {

                // 3. Load the fresh user state from the database
                // This ensures we don't issue new tokens if the account was deleted or locked
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UserPrincipal userPrincipal = (UserPrincipal) userDetails;

                // 4. Issue a brand new TokenPair (Refresh Token Rotation)
                return new TokenPair(
                        jwtUtil.generateToken(userPrincipal),
                        jwtUtil.generateRefreshToken(userPrincipal)
                );
            }
        }

        // Return null if validation fails; the AuthController will catch this and return 401
        return null;
    }
}