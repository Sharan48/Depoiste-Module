package com.scm2.repositaries;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scm2.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByRoleName(String role);

}
