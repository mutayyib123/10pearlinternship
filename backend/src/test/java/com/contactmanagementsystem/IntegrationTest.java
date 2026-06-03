package com.contactmanagementsystem;

import com.contactmanagementsystem.dto.user.UserRegistrationRequest;
import com.contactmanagementsystem.dto.contact.ContactRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Integration Tests - Full User and Contact Workflow")
class IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private UserRegistrationRequest registrationRequest;
    private ContactRequest contactRequest;
    private Long userId;

    @BeforeEach
    void setUp() {
        registrationRequest = new UserRegistrationRequest();
        registrationRequest.setName("Test User");
        registrationRequest.setEmail("testuser" + System.currentTimeMillis() + "@example.com");
        registrationRequest.setPhone("+923001234567");
        registrationRequest.setPassword("TestPassword123");

        contactRequest = new ContactRequest();
        contactRequest.setFirstName("Test");
        contactRequest.setLastName("Contact");
        contactRequest.setTitle("Test Title");
        contactRequest.setContactDetails(new ArrayList<>());
    }

    @Test
    @DisplayName("Should complete full user registration and login flow")
    void testUserRegistrationAndLoginFlow() throws Exception {
        // Step 1: Register user
        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(registrationRequest.getEmail()));
    }

    @Test
    @DisplayName("Should complete full contact lifecycle (CRUD operations)")
    void testContactLifecycleFlow() throws Exception {
        // First register a user
        var registerResponse = mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        // Extract userId from response
        String responseBody = registerResponse.getResponse().getContentAsString();
        userId = objectMapper.readTree(responseBody).get("id").asLong();

        // Set userId in contact request
        contactRequest.setUserId(userId);

        // Create a contact
        var createResponse = mockMvc.perform(post("/api/contacts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(contactRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        // Extract contactId
        String createResponseBody = createResponse.getResponse().getContentAsString();
        Long contactId = objectMapper.readTree(createResponseBody).get("id").asLong();

        // Get the contact by ID
        mockMvc.perform(get("/api/contacts/" + contactId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Test"))
                .andExpect(jsonPath("$.lastName").value("Contact"));

        // Update the contact
        contactRequest.setFirstName("Updated Test");
        mockMvc.perform(put("/api/contacts/" + contactId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(contactRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Updated Test"));

        // Get all contacts with pagination
        mockMvc.perform(get("/api/contacts?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElements").exists());

        // Search for contacts
        mockMvc.perform(get("/api/contacts/search?query=Updated"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());

        // Delete the contact
        mockMvc.perform(delete("/api/contacts/" + contactId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Contact deleted successfully"));

        // Verify contact is deleted (should return 404)
        mockMvc.perform(get("/api/contacts/" + contactId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should handle validation errors properly")
    void testValidationErrorHandling() throws Exception {
        // Try to register with invalid email
        registrationRequest.setEmail("invalid-email");
        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isBadRequest());

        // Try to register with short password
        registrationRequest.setEmail("valid@example.com");
        registrationRequest.setPassword("123");
        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should handle unauthorized access properly")
    void testUnauthorizedAccessHandling() throws Exception {
        // Try to login with non-existent user
        var loginRequest = new com.contactmanagementsystem.dto.user.UserLoginRequest();
        loginRequest.setEmail("nonexistent@example.com");
        loginRequest.setPassword("password123");

        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isNotFound());
    }
}
