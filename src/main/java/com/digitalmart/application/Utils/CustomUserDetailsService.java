package com.digitalmart.application.Utils;

import com.digitalmart.application.Auth.LoginDto;
import com.digitalmart.application.UserIdentity.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        LoginDto user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Bad Request"));
        return new UserPrincipal(
                user.id(),
                user.email(),
                user.passwordHash(),
                user.role().toString()
        );
    }
}
