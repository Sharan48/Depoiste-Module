package com.scm2.repositaries;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.scm2.entity.Contact;

public interface ContactRepository extends JpaRepository<Contact, Integer> {

    List<Contact> findByUserId(Integer userId);

    Optional<Contact> findByUserIdAndId(Integer userId, Integer id);

    Page<Contact> findByNameContaining(String name, Pageable pageable);

}
