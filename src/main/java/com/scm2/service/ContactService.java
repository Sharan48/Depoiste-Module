package com.scm2.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.scm2.dto.ContactDto;

public interface ContactService {
    ContactDto addContact(Integer userId, ContactDto contactDto);

    ContactDto updateContact(Integer userId, Integer id, ContactDto contactDto);

    String deleteContactById(Integer contactId);

    List<ContactDto> getConctsByUserID(Integer userId);

    ContactDto getContactByUserIdAndConatctId(Integer userId, Integer concatId);

    ContactDto getContactById(Integer contactId);

    List<ContactDto> getAllContactDos();

    Page<ContactDto> searchByName(String name, int size, int page, String sortBy, String sortOrder);

}