package com.contactmanagementsystem.service;

import com.contactmanagementsystem.dto.common.PageResponse;
import com.contactmanagementsystem.dto.contact.ContactDetailRequest;
import com.contactmanagementsystem.dto.contact.ContactRequest;
import com.contactmanagementsystem.dto.contact.ContactResponse;
import com.contactmanagementsystem.entity.Contact;
import com.contactmanagementsystem.entity.ContactCategory;
import com.contactmanagementsystem.entity.ContactDetails;
import com.contactmanagementsystem.entity.DetailType;
import com.contactmanagementsystem.entity.User;
import com.contactmanagementsystem.exception.ContactNotFoundException;
import com.contactmanagementsystem.exception.UserNotFoundException;
import com.contactmanagementsystem.repository.ContactRepository;
import com.contactmanagementsystem.repository.UserRepository;
import com.contactmanagementsystem.service.impl.ContactServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Contact Service Tests")
class ContactServiceImplTest {

    @Mock
    private ContactRepository contactRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ContactServiceImpl contactService;

    private User user;
    private Contact contact;
    private ContactDetails contactDetail;
    private ContactRequest contactRequest;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("Ahmed Ali")
                .email("ahmed@example.com")
                .phone("+923001234567")
                .password("$2a$10$hashedPassword")
                .build();

        contact = Contact.builder()
                .id(1L)
                .firstName("Ali")
                .lastName("Khan")
                .title("Software Engineer")
                .userId(user)
                .contactDetails(new ArrayList<>())
                .build();

        contactDetail = ContactDetails.builder()
                .id(1L)
                .contactId(contact)
                .type(DetailType.WORK)
                .value("ali.khan@company.com")
                .category(ContactCategory.EMAIL)
                .build();

        contact.getContactDetails().add(contactDetail);

        contactRequest = new ContactRequest();
        contactRequest.setFirstName("Ali");
        contactRequest.setLastName("Khan");
        contactRequest.setTitle("Software Engineer");
        contactRequest.setUserId(1L);
        contactRequest.setContactDetails(new ArrayList<>());

        pageable = PageRequest.of(0, 10);
    }

    // =====================================================
    // CREATE CONTACT TESTS
    // =====================================================

    @Test
    @DisplayName("Should create contact successfully")
    void testCreateContactSuccess() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(contactRepository.save(any(Contact.class))).thenReturn(contact);

        // Act
        ContactResponse response = contactService.createContact(contactRequest);

        // Assert
        assertNotNull(response);
        assertEquals("Ali", response.getFirstName());
        assertEquals("Khan", response.getLastName());
        assertEquals(1L, response.getUserId());
        verify(userRepository, times(1)).findById(1L);
        verify(contactRepository, times(1)).save(any(Contact.class));
    }

    @Test
    @DisplayName("Should throw exception when user not found during create")
    void testCreateContactUserNotFound() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> {
            contactService.createContact(contactRequest);
        });
        verify(contactRepository, never()).save(any(Contact.class));
    }

    // =====================================================
    // UPDATE CONTACT TESTS
    // =====================================================

    @Test
    @DisplayName("Should update contact successfully")
    void testUpdateContactSuccess() {
        // Arrange
        ContactRequest updateRequest = new ContactRequest();
        updateRequest.setFirstName("UpdatedAli");
        updateRequest.setLastName("UpdatedKhan");
        updateRequest.setTitle("Senior Engineer");
        updateRequest.setUserId(1L);
        updateRequest.setContactDetails(new ArrayList<>());

        when(contactRepository.findById(1L)).thenReturn(Optional.of(contact));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(contactRepository.save(any(Contact.class))).thenReturn(contact);

        // Act
        ContactResponse response = contactService.updateContact(1L, updateRequest);

        // Assert
        assertNotNull(response);
        verify(contactRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(1L);
        verify(contactRepository, times(1)).save(any(Contact.class));
    }

    @Test
    @DisplayName("Should throw exception when contact not found during update")
    void testUpdateContactNotFound() {
        // Arrange
        when(contactRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ContactNotFoundException.class, () -> {
            contactService.updateContact(1L, contactRequest);
        });
        verify(contactRepository, never()).save(any(Contact.class));
    }

    // =====================================================
    // DELETE CONTACT TESTS
    // =====================================================

    @Test
    @DisplayName("Should delete contact successfully")
    void testDeleteContactSuccess() {
        // Arrange
        when(contactRepository.findById(1L)).thenReturn(Optional.of(contact));
        doNothing().when(contactRepository).delete(any(Contact.class));

        // Act
        assertDoesNotThrow(() -> contactService.deleteContact(1L));

        // Assert
        verify(contactRepository, times(1)).findById(1L);
        verify(contactRepository, times(1)).delete(contact);
    }

    @Test
    @DisplayName("Should throw exception when contact not found during delete")
    void testDeleteContactNotFound() {
        // Arrange
        when(contactRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ContactNotFoundException.class, () -> {
            contactService.deleteContact(1L);
        });
        verify(contactRepository, never()).delete(any(Contact.class));
    }

    // =====================================================
    // GET CONTACT BY ID TESTS
    // =====================================================

    @Test
    @DisplayName("Should get contact by id successfully")
    void testGetContactByIdSuccess() {
        // Arrange
        when(contactRepository.findById(1L)).thenReturn(Optional.of(contact));

        // Act
        ContactResponse response = contactService.getContactById(1L);

        // Assert
        assertNotNull(response);
        assertEquals("Ali", response.getFirstName());
        assertEquals("Khan", response.getLastName());
        assertEquals(1, response.getContactDetails().size());
        verify(contactRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when contact not found")
    void testGetContactByIdNotFound() {
        // Arrange
        when(contactRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ContactNotFoundException.class, () -> {
            contactService.getContactById(1L);
        });
    }

    // =====================================================
    // GET ALL CONTACTS TESTS
    // =====================================================

    @Test
    @DisplayName("Should get all contacts with pagination")
    void testGetAllContactsSuccess() {
        // Arrange
        List<Contact> contacts = new ArrayList<>();
        contacts.add(contact);
        Page<Contact> page = new PageImpl<>(contacts, pageable, 1);
        when(contactRepository.findAll(pageable)).thenReturn(page);

        // Act
        PageResponse<ContactResponse> response = contactService.getAllContacts(pageable);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getContent().size());
        assertEquals(0, response.getPageNumber());
        assertEquals(1, response.getTotalPages());
        verify(contactRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("Should return empty page when no contacts exist")
    void testGetAllContactsEmpty() {
        // Arrange
        Page<Contact> emptyPage = new PageImpl<>(new ArrayList<>(), pageable, 0);
        when(contactRepository.findAll(pageable)).thenReturn(emptyPage);

        // Act
        PageResponse<ContactResponse> response = contactService.getAllContacts(pageable);

        // Assert
        assertNotNull(response);
        assertEquals(0, response.getContent().size());
        assertEquals(0, response.getTotalPages());
    }

    // =====================================================
    // SEARCH CONTACTS TESTS
    // =====================================================

    @Test
    @DisplayName("Should search contacts successfully")
    void testSearchContactsSuccess() {
        // Arrange
        List<Contact> contacts = new ArrayList<>();
        contacts.add(contact);
        Page<Contact> page = new PageImpl<>(contacts, pageable, 1);
        when(contactRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                "Ali", "Ali", pageable)).thenReturn(page);

        // Act
        PageResponse<ContactResponse> response = contactService.searchContacts("Ali", pageable);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getContent().size());
        verify(contactRepository, times(1)).findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                "Ali", "Ali", pageable);
    }

    @Test
    @DisplayName("Should return empty page when search has no results")
    void testSearchContactsNoResults() {
        // Arrange
        Page<Contact> emptyPage = new PageImpl<>(new ArrayList<>(), pageable, 0);
        when(contactRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                "NonExistent", "NonExistent", pageable)).thenReturn(emptyPage);

        // Act
        PageResponse<ContactResponse> response = contactService.searchContacts("NonExistent", pageable);

        // Assert
        assertNotNull(response);
        assertEquals(0, response.getContent().size());
    }
}
