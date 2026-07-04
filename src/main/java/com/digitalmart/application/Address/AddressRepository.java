package com.digitalmart.application.Address;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {

    Long countUserIdByUserId(Long id);
}
