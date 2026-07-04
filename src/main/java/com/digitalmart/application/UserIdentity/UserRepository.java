package com.digitalmart.application.UserIdentity;

import com.digitalmart.application.Auth.LoginDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByEmail(String email);

    @Query("SELECT u.id, u.email, u.passwordHash, u.role from User u Where u.email = :email")
    Optional<LoginDto> findByEmail(@Param("email") String email);

    @Query("SELECT DISTINCT u FROM User u JOIN FETCH u.addresses WHERE u.id = :id")
    Optional<User> findUserProfile(@Param("id") Long id);
}
