package com.scm2.repositaries;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scm2.dto.UserDto;
import com.scm2.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByphonenumber(String phonenumber);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    Optional<User> findByUsernameOrEmail(String username, String email);

    Optional<User> findByEmailToken(String emailToken);

    Optional<User> findByEmail(String email);

}
