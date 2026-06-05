package com.contactmanagementsystem.repository;

import com.contactmanagementsystem.entity.Contact;
import com.contactmanagementsystem.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("Contact Repository Tests")
class ContactRepositoryTest {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private Contact contact;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .name("Ahmed Ali")
                .email("ahmed@example.com")
                .phone("+923001234567")
                .password("$2a$10$hashedPassword")
                .build();
        userRepository.save(user);

        contact = Contact.builder()
                .firstName("Ali")
                .lastName("Khan")
                .title("Software Engineer")
                .userId(user)
                .build();

        pageable = PageRequest.of(0, 10);
    }

    @Test
    @DisplayName("Should save contact successfully")
    void testSaveContactSuccess() {
        // Act
        Contact savedContact = contactRepository.save(contact);

        // Assert
        assertNotNull(savedContact.getId());
        assertEquals("Ali", savedContact.getFirstName());
        assertEquals("Khan", savedContact.getLastName());
    }

    @Test
    @DisplayName("Should find contact by id")
    void testFindByIdSuccess() {
        // Arrange
        Contact savedContact = contactRepository.save(contact);

        // Act
        Optional<Contact> foundContact = contactRepository.findById(savedContact.getId());

        // Assert
        assertTrue(foundContact.isPresent());
        assertEquals("Ali", foundContact.get().getFirstName());
    }

    @Test
    @DisplayName("Should search contacts by firstName")
    void testSearchByFirstNameSuccess() {
        // Arrange
        contactRepository.save(contact);

        // Act
        Page<Contact> foundContacts = contactRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase("Ali", "Ali", pageable);

        // Assert
        assertEquals(1, foundContacts.getTotalElements());
        assertEquals("Ali", foundContacts.getContent().get(0).getFirstName());
    }

    @Test
    @DisplayName("Should search contacts by lastName")
    void testSearchByLastNameSuccess() {
        // Arrange
        contactRepository.save(contact);

        // Act
        Page<Contact> foundContacts = contactRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase("Khan", "Khan", pageable);

        // Assert
        assertEquals(1, foundContacts.getTotalElements());
        assertEquals("Khan", foundContacts.getContent().get(0).getLastName());
    }

    @Test
    @DisplayName("Should return empty page when search has no results")
    void testSearchNoResults() {
        // Arrange
        contactRepository.save(contact);

        // Act
        Page<Contact> foundContacts = contactRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase("NonExistent", "NonExistent", pageable);

        // Assert
        assertEquals(0, foundContacts.getTotalElements());
    }

    @Test
    @DisplayName("Should search case-insensitive")
    void testSearchCaseInsensitive() {
        // Arrange
        contactRepository.save(contact);

        // Act
        Page<Contact> foundContacts = contactRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase("ali", "ali", pageable);

        // Assert
        assertEquals(1, foundContacts.getTotalElements());
    }

    @Test
    @DisplayName("Should delete contact by id")
    void testDeleteContactSuccess() {
        // Arrange
        Contact savedContact = contactRepository.save(contact);
        Long contactId = savedContact.getId();

        // Act
        contactRepository.deleteById(contactId);
        Optional<Contact> deletedContact = contactRepository.findById(contactId);

        // Assert
        assertFalse(deletedContact.isPresent());
    }

    @Test
    @DisplayName("Should update contact successfully")
    void testUpdateContactSuccess() {
        // Arrange
        Contact savedContact = contactRepository.save(contact);
        savedContact.setFirstName("Updated Ali");

        // Act
        Contact updatedContact = contactRepository.save(savedContact);

        // Assert
        assertEquals("Updated Ali", updatedContact.getFirstName());
    }

    @Test
    @DisplayName("Should get all contacts with pagination")
    void testFindAllWithPagination() {
        // Arrange
        contactRepository.save(contact);
        Contact contact2 = Contact.builder()
                .firstName("Sara")
                .lastName("Ahmed")
                .title("Project Manager")
                .userId(user)
                .build();
        contactRepository.save(contact2);

        // Act
        Page<Contact> allContacts = contactRepository.findAll(pageable);

        // Assert
        assertEquals(2, allContacts.getTotalElements());
        assertEquals(1, allContacts.getTotalPages());
    }
}
