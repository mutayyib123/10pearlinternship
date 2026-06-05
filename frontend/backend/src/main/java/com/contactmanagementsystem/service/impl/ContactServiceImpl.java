package com.contactmanagementsystem.service.impl;

import com.contactmanagementsystem.dto.common.PageResponse;
import com.contactmanagementsystem.dto.contact.ContactDetailRequest;
import com.contactmanagementsystem.dto.contact.ContactDetailResponse;
import com.contactmanagementsystem.dto.contact.ContactRequest;
import com.contactmanagementsystem.dto.contact.ContactResponse;
import com.contactmanagementsystem.entity.Contact;
import com.contactmanagementsystem.entity.ContactDetails;
import com.contactmanagementsystem.entity.User;
import com.contactmanagementsystem.exception.ContactNotFoundException;
import com.contactmanagementsystem.exception.UserNotFoundException;
import com.contactmanagementsystem.repository.ContactRepository;
import com.contactmanagementsystem.repository.UserRepository;
import com.contactmanagementsystem.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {

    private static final Logger log = LoggerFactory.getLogger(ContactServiceImpl.class);

    private final ContactRepository contactRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ContactResponse createContact(ContactRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + request.getUserId()));

        Contact contact = Contact.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .title(request.getTitle())
                .userId(user)
                .contactDetails(new ArrayList<>())
                .build();

        addDetailsToContact(contact, request.getContactDetails());
        Contact savedContact = contactRepository.save(contact);
        log.info("Contact created successfully with id: {}", savedContact.getId());
        return toContactResponse(savedContact);
    }

    @Override
    @Transactional
    public ContactResponse updateContact(Long id, ContactRequest request) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new ContactNotFoundException("Contact not found with id: " + id));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + request.getUserId()));

        contact.setFirstName(request.getFirstName());
        contact.setLastName(request.getLastName());
        contact.setTitle(request.getTitle());
        contact.setUserId(user);
        contact.getContactDetails().clear();
        addDetailsToContact(contact, request.getContactDetails());

        Contact updatedContact = contactRepository.save(contact);
        log.info("Contact updated successfully with id: {}", updatedContact.getId());
        return toContactResponse(updatedContact);
    }

    @Override
    @Transactional
    public void deleteContact(Long id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new ContactNotFoundException("Contact not found with id: " + id));

        contactRepository.delete(contact);
        log.info("Contact deleted successfully with id: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public ContactResponse getContactById(Long id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new ContactNotFoundException("Contact not found with id: " + id));
        return toContactResponse(contact);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ContactResponse> getAllContacts(Pageable pageable) {
        Page<Contact> page = contactRepository.findAll(pageable);
        return toPageResponse(page);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ContactResponse> searchContacts(String query, Pageable pageable) {
        Page<Contact> page = contactRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(query, query, pageable);
        return toPageResponse(page);
    }

    private void addDetailsToContact(Contact contact, List<ContactDetailRequest> details) {
        if (details == null) {
            return;
        }

        for (ContactDetailRequest detailRequest : details) {
            ContactDetails detail = ContactDetails.builder()
                    .contactId(contact)
                    .type(detailRequest.getType())
                    .value(detailRequest.getValue())
                    .category(detailRequest.getCategory())
                    .build();
            contact.getContactDetails().add(detail);
        }
    }

    private ContactResponse toContactResponse(Contact contact) {
        List<ContactDetailResponse> details = new ArrayList<>();
        if (contact.getContactDetails() != null) {
            for (ContactDetails detail : contact.getContactDetails()) {
                details.add(ContactDetailResponse.builder()
                        .id(detail.getId())
                        .type(detail.getType())
                        .value(detail.getValue())
                        .category(detail.getCategory())
                        .build());
            }
        }

        return ContactResponse.builder()
                .id(contact.getId())
                .firstName(contact.getFirstName())
                .lastName(contact.getLastName())
                .title(contact.getTitle())
                .userId(contact.getUserId().getId())
                .contactDetails(details)
                .build();
    }

    private PageResponse<ContactResponse> toPageResponse(Page<Contact> page) {
        List<ContactResponse> content = page.getContent().stream().map(this::toContactResponse).toList();
        return PageResponse.<ContactResponse>builder()
                .content(content)
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }
}