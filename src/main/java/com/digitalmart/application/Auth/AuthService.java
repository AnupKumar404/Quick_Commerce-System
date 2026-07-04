package com.digitalmart.application.Auth;

import com.digitalmart.application.Utils.JwtUtil;
import com.digitalmart.application.Utils.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    //Logic of Login
    public String login(LoginRequest req) throws AuthenticationException {

//        log.info(req.email(), req.passwordHash());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.email(), req.passwordHash())
        );

        UserPrincipal user = (UserPrincipal) authentication.getPrincipal();


        if(user != null) {
            jwtUtil.generateToken(user);
            return "You are logged in";
        }

        throw new BadCredentialsException("");
    }
}
