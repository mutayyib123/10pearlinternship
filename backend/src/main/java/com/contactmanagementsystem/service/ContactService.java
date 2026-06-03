package com.contactmanagementsystem.service;

import com.contactmanagementsystem.dto.common.PageResponse;
import com.contactmanagementsystem.dto.contact.ContactRequest;
import com.contactmanagementsystem.dto.contact.ContactResponse;
import org.springframework.data.domain.Pageable;

public interface ContactService {

    ContactResponse createContact(ContactRequest request);

    ContactResponse updateContact(Long id, ContactRequest request);

    void deleteContact(Long id);

    ContactResponse getContactById(Long id);

    PageResponse<ContactResponse> getAllContacts(Pageable pageable);

    PageResponse<ContactResponse> searchContacts(String query, Pageable pageable);
}