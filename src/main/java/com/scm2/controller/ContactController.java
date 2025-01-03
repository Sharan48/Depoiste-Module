package com.scm2.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scm2.dto.ContactDto;
import com.scm2.dto.PaginationResponse;
import com.scm2.service.ContactService;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/contacts")
public class ContactController {

    private ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping("/{userid}/savecontact")
    public ResponseEntity<ContactDto> addContact(@PathVariable(name = "userid") Integer userid,
            @RequestBody ContactDto contactDto) {
        ContactDto contacts = contactService.addContact(userid, contactDto);
        return new ResponseEntity<>(contacts, HttpStatus.CREATED);
    }

    @PutMapping("{userid}/contact/{contactId}")
    public ResponseEntity<ContactDto> updateContact(@PathVariable(name = "userid") Integer userid,
            @PathVariable(name = "contactId") Integer contactId, @RequestBody ContactDto contact) {
        ContactDto conatctdto = contactService.updateContact(userid, contactId, contact);
        return new ResponseEntity<>(conatctdto, HttpStatus.ACCEPTED);

    }

    @GetMapping("/contactslist")
    public ResponseEntity<List<ContactDto>> getListOfContacts() {
        List<ContactDto> list = contactService.getAllContactDos();
        return new ResponseEntity<>(list, HttpStatus.OK);

    }

    @DeleteMapping("/deletecontact/{id}")
    public ResponseEntity<String> deleteContactById(
            @PathVariable(name = "id") Integer id) {
        String delete = contactService.deleteContactById(id);
        return new ResponseEntity<>(delete, HttpStatus.OK);

    }

    @GetMapping("/{userid}/contacts")
    public ResponseEntity<List<ContactDto>> getContactByUserId(@PathVariable(name = "userid") Integer user) {
        List<ContactDto> contactDtos = contactService.getConctsByUserID(user);
        return new ResponseEntity<>(contactDtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContactDto> getContactById(@PathVariable(name = "id") Integer contactId) {
        ContactDto contact = contactService.getContactById(contactId);
        return new ResponseEntity<>(contact, HttpStatus.OK);
    }

    @GetMapping("/{userId}/contact/{contactId}")
    public ResponseEntity<ContactDto> getContactByUserIdAndConatctId(@PathVariable(name = "userId") Integer userId,
            @PathVariable(name = "contactId") Integer contactId) {
        ContactDto contactDto = contactService.getContactByUserIdAndConatctId(userId, contactId);
        return new ResponseEntity<>(contactDto, HttpStatus.ACCEPTED);
    }

    @GetMapping("/search")
    public PaginationResponse<ContactDto> searchByName(
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            @RequestParam(value = "name") String value) {

        Page<ContactDto> nameSearch = contactService.searchByName(value, size, page, sortBy, direction);

        return new PaginationResponse<>(nameSearch.getContent(), nameSearch.getNumber(), nameSearch.getSize(),
                nameSearch.getTotalPages());

    }

}
