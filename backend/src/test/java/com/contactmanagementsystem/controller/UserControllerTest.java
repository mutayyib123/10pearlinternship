package com.contactmanagementsystem.controller;

import com.contactmanagementsystem.dto.common.MessageResponse;
import com.contactmanagementsystem.dto.user.AuthResponse;
import com.contactmanagementsystem.dto.user.ChangePasswordRequest;
import com.contactmanagementsystem.dto.user.UserLoginRequest;
import com.contactmanagementsystem.dto.user.UserRegistrationRequest;
import com.contactmanagementsystem.dto.user.UserResponse;
import com.contactmanagementsystem.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("User Controller Tests")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private UserRegistrationRequest registrationRequest;
    private UserLoginRequest loginRequest;
    private ChangePasswordRequest changePasswordRequest;
    private UserResponse userResponse;
    private AuthResponse authResponse;

    @BeforeEach
    void setUp() {
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

        userResponse = UserResponse.builder()
                .id(1L)
                .name("Ahmed Ali")
                .email("ahmed@example.com")
                .phone("+923001234567")
                .build();

        authResponse = AuthResponse.builder()
                .message("Login successful")
                .user(userResponse)
                .build();
    }

    // =====================================================
    // REGISTRATION TESTS
    // =====================================================

    @Test
    @DisplayName("Should register user successfully")
    void testRegisterUserSuccess() throws Exception {
        // Arrange
        when(userService.registerUser(any())).thenReturn(userResponse);

        // Act & Assert
        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Ahmed Ali"))
                .andExpect(jsonPath("$.email").value("ahmed@example.com"));
    }

    @Test
    @DisplayName("Should return 400 when email is invalid")
    void testRegisterUserInvalidEmail() throws Exception {
        // Arrange
        registrationRequest.setEmail("invalid-email");

        // Act & Assert
        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when name is null")
    void testRegisterUserNullName() throws Exception {
        // Arrange
        registrationRequest.setName(null);

        // Act & Assert
        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when password is too short")
    void testRegisterUserShortPassword() throws Exception {
        // Arrange
        registrationRequest.setPassword("123");

        // Act & Assert
        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isBadRequest());
    }

    // =====================================================
    // LOGIN TESTS
    // =====================================================

    @Test
    @DisplayName("Should login user successfully")
    void testLoginUserSuccess() throws Exception {
        // Arrange
        when(userService.loginUser(any())).thenReturn(authResponse);

        // Act & Assert
        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login successful"))
                .andExpect(jsonPath("$.user.id").value(1))
                .andExpect(jsonPath("$.user.email").value("ahmed@example.com"));
    }

    @Test
    @DisplayName("Should return 400 when email is missing")
    void testLoginUserMissingEmail() throws Exception {
        // Arrange
        loginRequest.setEmail(null);

        // Act & Assert
        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when password is missing")
    void testLoginUserMissingPassword() throws Exception {
        // Arrange
        loginRequest.setPassword(null);

        // Act & Assert
        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest());
    }

    // =====================================================
    // CHANGE PASSWORD TESTS
    // =====================================================

    @Test
    @DisplayName("Should change password successfully")
    void testChangePasswordSuccess() throws Exception {
        // Arrange
        doNothing().when(userService).changePassword(any());

        // Act & Assert
        mockMvc.perform(put("/api/users/change-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(changePasswordRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Password changed successfully"));
    }

    @Test
    @DisplayName("Should return 400 when email is null")
    void testChangePasswordNullEmail() throws Exception {
        // Arrange
        changePasswordRequest.setEmail(null);

        // Act & Assert
        mockMvc.perform(put("/api/users/change-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(changePasswordRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when current password is null")
    void testChangePasswordNullCurrentPassword() throws Exception {
        // Arrange
        changePasswordRequest.setCurrentPassword(null);

        // Act & Assert
        mockMvc.perform(put("/api/users/change-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(changePasswordRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when new password is null")
    void testChangePasswordNullNewPassword() throws Exception {
        // Arrange
        changePasswordRequest.setNewPassword(null);

        // Act & Assert
        mockMvc.perform(put("/api/users/change-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(changePasswordRequest)))
                .andExpect(status().isBadRequest());
    }
}
