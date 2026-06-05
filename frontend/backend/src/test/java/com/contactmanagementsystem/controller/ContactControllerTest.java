package com.contactmanagementsystem.controller;

import com.contactmanagementsystem.dto.common.PageResponse;
import com.contactmanagementsystem.dto.contact.ContactDetailResponse;
import com.contactmanagementsystem.dto.contact.ContactRequest;
import com.contactmanagementsystem.dto.contact.ContactResponse;
import com.contactmanagementsystem.entity.ContactCategory;
import com.contactmanagementsystem.entity.DetailType;
import com.contactmanagementsystem.service.ContactService;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Contact Controller Tests")
class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ContactService contactService;

    private ContactRequest contactRequest;
    private ContactResponse contactResponse;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        contactRequest = new ContactRequest();
        contactRequest.setFirstName("Ali");
        contactRequest.setLastName("Khan");
        contactRequest.setTitle("Software Engineer");
        contactRequest.setUserId(1L);
        contactRequest.setContactDetails(new ArrayList<>());

        contactResponse = ContactResponse.builder()
                .id(1L)
                .firstName("Ali")
                .lastName("Khan")
                .title("Software Engineer")
                .userId(1L)
                .contactDetails(new ArrayList<>())
                .build();

        pageable = PageRequest.of(0, 10);
    }

    // =====================================================
    // CREATE CONTACT TESTS
    // =====================================================

    @Test
    @DisplayName("Should create contact successfully")
    void testCreateContactSuccess() throws Exception {
        // Arrange
        when(contactService.createContact(any())).thenReturn(contactResponse);

        // Act & Assert
        mockMvc.perform(post("/api/contacts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(contactRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Ali"))
                .andExpect(jsonPath("$.lastName").value("Khan"));
    }

    @Test
    @DisplayName("Should return 400 when firstName is null")
    void testCreateContactNullFirstName() throws Exception {
        // Arrange
        contactRequest.setFirstName(null);

        // Act & Assert
        mockMvc.perform(post("/api/contacts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(contactRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when lastName is null")
    void testCreateContactNullLastName() throws Exception {
        // Arrange
        contactRequest.setLastName(null);

        // Act & Assert
        mockMvc.perform(post("/api/contacts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(contactRequest)))
                .andExpect(status().isBadRequest());
    }

    // =====================================================
    // UPDATE CONTACT TESTS
    // =====================================================

    @Test
    @DisplayName("Should update contact successfully")
    void testUpdateContactSuccess() throws Exception {
        // Arrange
        when(contactService.updateContact(anyLong(), any())).thenReturn(contactResponse);

        // Act & Assert
        mockMvc.perform(put("/api/contacts/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(contactRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Ali"));
    }

    // =====================================================
    // DELETE CONTACT TESTS
    // =====================================================

    @Test
    @DisplayName("Should delete contact successfully")
    void testDeleteContactSuccess() throws Exception {
        // Arrange
        doNothing().when(contactService).deleteContact(anyLong());

        // Act & Assert
        mockMvc.perform(delete("/api/contacts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Contact deleted successfully"));
    }

    // =====================================================
    // GET CONTACT BY ID TESTS
    // =====================================================

    @Test
    @DisplayName("Should get contact by id successfully")
    void testGetContactByIdSuccess() throws Exception {
        // Arrange
        when(contactService.getContactById(anyLong())).thenReturn(contactResponse);

        // Act & Assert
        mockMvc.perform(get("/api/contacts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Ali"))
                .andExpect(jsonPath("$.lastName").value("Khan"));
    }

    // =====================================================
    // GET ALL CONTACTS TESTS
    // =====================================================

    @Test
    @DisplayName("Should get all contacts with pagination")
    void testGetAllContactsSuccess() throws Exception {
        // Arrange
        List<ContactResponse> contactList = Arrays.asList(contactResponse);
        PageResponse<ContactResponse> pageResponse = PageResponse.<ContactResponse>builder()
                .content(contactList)
                .pageNumber(0)
                .pageSize(10)
                .totalElements(1)
                .totalPages(1)
                .build();
        when(contactService.getAllContacts(any(Pageable.class))).thenReturn(pageResponse);

        // Act & Assert
        mockMvc.perform(get("/api/contacts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1));
    }

    @Test
    @DisplayName("Should handle pagination parameters")
    void testGetAllContactsWithPagination() throws Exception {
        // Arrange
        PageResponse<ContactResponse> pageResponse = PageResponse.<ContactResponse>builder()
                .content(new ArrayList<>())
                .pageNumber(1)
                .pageSize(5)
                .totalElements(0)
                .totalPages(0)
                .build();
        when(contactService.getAllContacts(any(Pageable.class))).thenReturn(pageResponse);

        // Act & Assert
        mockMvc.perform(get("/api/contacts?page=1&size=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pageNumber").value(1))
                .andExpect(jsonPath("$.pageSize").value(5));
    }

    // =====================================================
    // SEARCH CONTACTS TESTS
    // =====================================================

    @Test
    @DisplayName("Should search contacts successfully")
    void testSearchContactsSuccess() throws Exception {
        // Arrange
        List<ContactResponse> searchResults = Arrays.asList(contactResponse);
        PageResponse<ContactResponse> pageResponse = PageResponse.<ContactResponse>builder()
                .content(searchResults)
                .pageNumber(0)
                .pageSize(10)
                .totalElements(1)
                .totalPages(1)
                .build();
        when(contactService.searchContacts(anyString(), any(Pageable.class))).thenReturn(pageResponse);

        // Act & Assert
        mockMvc.perform(get("/api/contacts/search?query=Ali"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].firstName").value("Ali"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    @DisplayName("Should return empty page when search has no results")
    void testSearchContactsNoResults() throws Exception {
        // Arrange
        PageResponse<ContactResponse> emptyPageResponse = PageResponse.<ContactResponse>builder()
                .content(new ArrayList<>())
                .pageNumber(0)
                .pageSize(10)
                .totalElements(0)
                .totalPages(0)
                .build();
        when(contactService.searchContacts(anyString(), any(Pageable.class))).thenReturn(emptyPageResponse);

        // Act & Assert
        mockMvc.perform(get("/api/contacts/search?query=NonExistent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElements").value(0));
    }
}
