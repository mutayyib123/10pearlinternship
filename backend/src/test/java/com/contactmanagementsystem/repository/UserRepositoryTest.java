package com.contactmanagementsystem.repository;

import com.contactmanagementsystem.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("User Repository Tests")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .name("Ahmed Ali")
                .email("ahmed@example.com")
                .phone("+923001234567")
                .password("$2a$10$hashedPassword")
                .build();
    }

    @Test
    @DisplayName("Should save user successfully")
    void testSaveUserSuccess() {
        // Act
        User savedUser = userRepository.save(user);

        // Assert
        assertNotNull(savedUser.getId());
        assertEquals("Ahmed Ali", savedUser.getName());
        assertEquals("ahmed@example.com", savedUser.getEmail());
    }

    @Test
    @DisplayName("Should find user by email")
    void testFindByEmailSuccess() {
        // Arrange
        userRepository.save(user);

        // Act
        Optional<User> foundUser = userRepository.findByEmail("ahmed@example.com");

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals("Ahmed Ali", foundUser.get().getName());
    }

    @Test
    @DisplayName("Should return empty when email not found")
    void testFindByEmailNotFound() {
        // Act
        Optional<User> foundUser = userRepository.findByEmail("notexist@example.com");

        // Assert
        assertFalse(foundUser.isPresent());
    }

    @Test
    @DisplayName("Should check if email exists")
    void testExistsByEmailTrue() {
        // Arrange
        userRepository.save(user);

        // Act
        boolean exists = userRepository.existsByEmail("ahmed@example.com");

        // Assert
        assertTrue(exists);
    }

    @Test
    @DisplayName("Should return false when email does not exist")
    void testExistsByEmailFalse() {
        // Act
        boolean exists = userRepository.existsByEmail("notexist@example.com");

        // Assert
        assertFalse(exists);
    }

    @Test
    @DisplayName("Should find user by id")
    void testFindByIdSuccess() {
        // Arrange
        User savedUser = userRepository.save(user);

        // Act
        Optional<User> foundUser = userRepository.findById(savedUser.getId());

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals("Ahmed Ali", foundUser.get().getName());
    }

    @Test
    @DisplayName("Should delete user by id")
    void testDeleteById() {
        // Arrange
        User savedUser = userRepository.save(user);
        Long userId = savedUser.getId();

        // Act
        userRepository.deleteById(userId);
        Optional<User> deletedUser = userRepository.findById(userId);

        // Assert
        assertFalse(deletedUser.isPresent());
    }

    @Test
    @DisplayName("Should update user successfully")
    void testUpdateUserSuccess() {
        // Arrange
        User savedUser = userRepository.save(user);
        savedUser.setName("Updated Ahmed");

        // Act
        User updatedUser = userRepository.save(savedUser);

        // Assert
        assertEquals("Updated Ahmed", updatedUser.getName());
    }
}
