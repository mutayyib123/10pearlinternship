package com.contactmanagementsystem.service.impl;

import com.contactmanagementsystem.dto.contact.ContactRequest;
import com.contactmanagementsystem.entity.Contact;
import com.contactmanagementsystem.entity.User;
import com.contactmanagementsystem.repository.ContactRepository;
import com.contactmanagementsystem.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContactServiceImplTest {

    @Mock
    private ContactRepository contactRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ContactServiceImpl contactService;

    @Test
    void createContact_success() {
        ContactRequest req = new ContactRequest();
        req.setFirstName("Bob");
        req.setLastName("Smith");
        req.setTitle("Mr");
        req.setUserId(1L);

        User user = User.builder().id(1L).build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(contactRepository.save(any(Contact.class))).thenAnswer(i -> {
            Contact c = i.getArgument(0);
            c.setId(2L);
            return c;
        });

        var res = contactService.createContact(req);
        assertNotNull(res);
        assertEquals(2L, res.getId());
        verify(contactRepository).save(any(Contact.class));
    }

    @Test
    void getAllContacts_returnsPage() {
        when(contactRepository.findAll(PageRequest.of(0,10))).thenReturn(new PageImpl<>(List.of(new Contact())));
        var page = contactService.getAllContacts(PageRequest.of(0,10));
        assertNotNull(page);
        assertEquals(1, page.getContent().size());
    }
}
