package com.digitalmart.application.UserIdentity;

import com.digitalmart.application.Address.AddressDto;
import com.digitalmart.application.services.EmailService;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public String register(UserDao dto) throws DuplicateRequestException, BadRequestException {

        if(userRepository.existsByEmail(dto.email())){
            throw new DuplicateRequestException("Bad Request");
        }

        User newUser = User.builder()
                .firstname(dto.firstname())
                .lastname(dto.lastname())
                .email(dto.email())
                .passwordHash(passwordEncoder.encode(dto.passwordHash()))
                .gender(GenderType.valueOf(dto.gender()))
                .role(UserRole.Admin)
                .phoneNumber(dto.phoneNumber())
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(newUser);

        emailService.sendEmail(newUser.getEmail(), newUser.getFirstname());

        return "Registration Successful";
    }



    @Transactional(readOnly = true)
    public UserProfileDto getUserProfile(Long userId) throws NoSuchElementException{

        log.info("User id is {}", userId);

        User user = userRepository.findUserProfile(userId)
                .orElseThrow(() -> new NoSuchElementException("Not Found"));

        Set<AddressDto> addresses = user.getAddresses().stream()
                .map(address -> new AddressDto(
                        address.getId(),
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
                user.getGender().toString(),
                user.getPhoneNumber(),
                addresses
        );
    }
}
