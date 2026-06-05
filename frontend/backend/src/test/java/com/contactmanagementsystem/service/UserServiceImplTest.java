package com.contactmanagementsystem.service;

import com.contactmanagementsystem.dto.user.AuthResponse;
import com.contactmanagementsystem.dto.user.ChangePasswordRequest;
import com.contactmanagementsystem.dto.user.UserLoginRequest;
import com.contactmanagementsystem.dto.user.UserRegistrationRequest;
import com.contactmanagementsystem.dto.user.UserResponse;
import com.contactmanagementsystem.entity.User;
import com.contactmanagementsystem.exception.InvalidCredentialsException;
import com.contactmanagementsystem.exception.UserNotFoundException;
import com.contactmanagementsystem.repository.UserRepository;
import com.contactmanagementsystem.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("User Service Tests")
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserRegistrationRequest registrationRequest;
    private UserLoginRequest loginRequest;
    private ChangePasswordRequest changePasswordRequest;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("Ahmed Ali")
                .email("ahmed@example.com")
                .phone("+923001234567")
                .password("$2a$10$hashedPassword")
                .build();

        registrationRequest = new UserRegistrationRequest();
        registrationRequest.setName("Ahmed Ali");
        registrationRequest.setEmail("ahmed@example.com");
        registrationRequest.setPhone("+923001234567");
        registrationRequest.setPassword("password123");

        loginRequest = new UserLoginRequest();
        loginRequest.setEmail("ahmed@example.com");
        loginRequest.setPassword("password123");

        changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setEmail("ahmed@example.com");
        changePasswordRequest.setCurrentPassword("password123");
        changePasswordRequest.setNewPassword("newPassword123");
    }

    // =====================================================
    // REGISTRATION TESTS
    // =====================================================

    @Test
    @DisplayName("Should register user successfully")
    void testRegisterUserSuccess() {
        // Arrange
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("$2a$10$hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        UserResponse response = userService.registerUser(registrationRequest);

        // Assert
        assertNotNull(response);
        assertEquals("Ahmed Ali", response.getName());
        assertEquals("ahmed@example.com", response.getEmail());
        verify(userRepository, times(1)).existsByEmail("ahmed@example.com");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception when email already registered")
    void testRegisterUserEmailAlreadyExists() {
        // Arrange
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(InvalidCredentialsException.class, () -> {
            userService.registerUser(registrationRequest);
        });
        verify(userRepository, times(1)).existsByEmail("ahmed@example.com");
        verify(userRepository, never()).save(any(User.class));
    }

    // =====================================================
    // LOGIN TESTS
    // =====================================================

    @Test
    @DisplayName("Should login user successfully")
    void testLoginUserSuccess() {
        // Arrange
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "$2a$10$hashedPassword")).thenReturn(true);

        // Act
        AuthResponse response = userService.loginUser(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals("Login successful", response.getMessage());
        assertEquals("Ahmed Ali", response.getUser().getName());
        assertEquals("ahmed@example.com", response.getUser().getEmail());
        verify(userRepository, times(1)).findByEmail("ahmed@example.com");
    }

    @Test
    @DisplayName("Should throw exception when user not found during login")
    void testLoginUserNotFound() {
        // Arrange
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> {
            userService.loginUser(loginRequest);
        });
        verify(userRepository, times(1)).findByEmail("ahmed@example.com");
    }

    @Test
    @DisplayName("Should throw exception when password is incorrect")
    void testLoginInvalidPassword() {
        // Arrange
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "$2a$10$hashedPassword")).thenReturn(false);

        // Act & Assert
        assertThrows(InvalidCredentialsException.class, () -> {
            userService.loginUser(loginRequest);
        });
        verify(userRepository, times(1)).findByEmail("ahmed@example.com");
    }

    // =====================================================
    // CHANGE PASSWORD TESTS
    // =====================================================

    @Test
    @DisplayName("Should change password successfully")
    void testChangePasswordSuccess() {
        // Arrange
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "$2a$10$hashedPassword")).thenReturn(true);
        when(passwordEncoder.encode("newPassword123")).thenReturn("$2a$10$newHashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        assertDoesNotThrow(() -> userService.changePassword(changePasswordRequest));

        // Assert
        verify(userRepository, times(1)).findByEmail("ahmed@example.com");
        verify(passwordEncoder, times(1)).matches("password123", "$2a$10$hashedPassword");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception when user not found during password change")
    void testChangePasswordUserNotFound() {
        // Arrange
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> {
            userService.changePassword(changePasswordRequest);
        });
    }

    @Test
    @DisplayName("Should throw exception when current password is incorrect")
    void testChangePasswordIncorrectCurrentPassword() {
        // Arrange
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "$2a$10$hashedPassword")).thenReturn(false);

        // Act & Assert
        assertThrows(InvalidCredentialsException.class, () -> {
            userService.changePassword(changePasswordRequest);
        });
        verify(userRepository, never()).save(any(User.class));
    }
}
