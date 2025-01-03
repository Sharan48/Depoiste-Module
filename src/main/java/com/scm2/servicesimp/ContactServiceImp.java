package com.scm2.servicesimp;

import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.scm2.dto.ContactDto;
import com.scm2.dto.PaginationResponse;
import com.scm2.entity.Contact;
import com.scm2.entity.User;
import com.scm2.exception.ResourceNotFoundException;
import com.scm2.repositaries.ContactRepository;
import com.scm2.repositaries.UserRepository;
import com.scm2.service.ContactService;

@Service
public class ContactServiceImp implements ContactService {

    private ContactRepository contactRepository;
    private ModelMapper modelMapper;
    private UserRepository userRepository;

    public ContactServiceImp(ContactRepository contactRepository, ModelMapper modelMapper,
            UserRepository userRepository) {
        this.contactRepository = contactRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }

    @Override
    public ContactDto addContact(Integer userId, ContactDto contactDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user", "id", userId));
        Contact contact = modelMapper.map(contactDto, Contact.class);
        contact.setUser(user);
        Contact savecontact = contactRepository.save(contact);
        return modelMapper.map(savecontact, ContactDto.class);
    }

    @Override
    public ContactDto updateContact(Integer userid, Integer id, ContactDto contactDto) {

        Contact contact = contactRepository.findByUserIdAndId(userid, id)
                .orElseThrow(() -> new ResourceNotFoundException("contact", "id", id));

        if (contactDto.getName() != null) {
            contact.setName(contactDto.getName());

        }
        if (contactDto.getPhonenumber() != null) {
            contact.setPhonenumber(contactDto.getPhonenumber());
        }

        if (contactDto.getPicture() != null) {
            contact.setPicture(contactDto.getPicture());
        }
        Contact savecontact = contactRepository.save(contact);
        return modelMapper.map(savecontact, ContactDto.class);
    }

    @Override
    public String deleteContactById(Integer id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact", "id", id));
        contactRepository.delete(contact);
        return "Contact deleted successfully";
    }

    @Override
    public List<ContactDto> getAllContactDos() {
        List<Contact> listOfContact = contactRepository.findAll();
        List<ContactDto> dtoContact = listOfContact.stream()
                .map(contact -> modelMapper.map(contact, ContactDto.class)).collect(Collectors.toList());
        return dtoContact;
    }

    @Override
    public List<ContactDto> getConctsByUserID(Integer userId) {
        List<Contact> contactlist = contactRepository.findByUserId(userId);
        List<ContactDto> list = contactlist.stream().map(contact -> modelMapper.map(contact, ContactDto.class))
                .collect(Collectors.toList());
        return list;
    }

    @Override
    public ContactDto getContactById(Integer contactId) {
        Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new ResourceNotFoundException("contact", "id", contactId));
        ContactDto contacDto = modelMapper.map(contact, ContactDto.class);
        return contacDto;
    }

    @Override
    public ContactDto getContactByUserIdAndConatctId(Integer userId, Integer contactId) {
        Contact dtoContact = contactRepository.findByUserIdAndId(userId, contactId)
                .orElseThrow(() -> new ResourceNotFoundException("contact", "id", contactId));
        return modelMapper.map(dtoContact, ContactDto.class);
    }

    @Override
    public Page<ContactDto> searchByName(String name, int size, int page, String sortBy, String Order) {
        Sort sort = Order.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        PageRequest pageable = PageRequest.of(page, size, sort);
        Page<Contact> contactpage = contactRepository.findByNameContaining(name, pageable);
        return contactpage.map(contact -> modelMapper.map(contact, ContactDto.class));

    }

}
