package com.digitalmart.application.UserIdentity;

import com.digitalmart.application.Address.AddressDto;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDto register(UserDto dto) throws DuplicateRequestException {

        if(userRepository.existsByEmail(dto.email())){
            throw new DuplicateRequestException("Bad Request");
        }

        User newUser = User.builder()
                .firstname(dto.firstname())
                .lastname(dto.lastname())
                .email(dto.email())
                .passwordHash(passwordEncoder.encode(dto.passwordHash()))
                .role(UserRole.Admin)
                .phoneNumber(dto.phoneNumber())
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(newUser);

        return new UserResponseDto(
                newUser.getFirstname(),
                newUser.getLastname(),
                newUser.getEmail(),
                newUser.getPhoneNumber()
        );
    }



    @Transactional(readOnly = true)
    public UserProfileDto getUserProfile(Long id) throws DuplicateRequestException{

        User user = userRepository.findUserProfile(id)
                .orElseThrow();

        Set<AddressDto> addresses = user.getAddresses().stream()
                .map(address -> new AddressDto(
                        address.getAddressLine1(),
                        address.getAddressLine2(),
                        address.getCity(),
                        address.getState(),
                        address.getPinCode(),
                        address.getType().toString()
                ))
                .collect(Collectors.toSet());

        return new UserProfileDto(
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                user.getPhoneNumber(),
                addresses
        );
    }
}
