package com.digitalmart.application.Address;

import com.digitalmart.application.UserIdentity.User;
import com.digitalmart.application.UserIdentity.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public String addAddress(Long userId, AddressDto dto) throws BadRequestException {

        User userReference = userRepository.getReferenceById(userId);

        if(addressRepository.countUserIdByUserId(userId) >= 2){
            throw new BadRequestException("You can't add addresses more than 2!");
        }

        Address address = Address.builder()
                .addressLine1(dto.addressLine1())
                .addressLine2(dto.addressLine2())
                .city(dto.city())
                .user(userReference)
                .state(dto.state())
                .pinCode(dto.pinCode())
                .type(AddressType.valueOf(dto.type()))
                .build();

        addressRepository.save(address);

        return "Address saved successfully";
    }
}