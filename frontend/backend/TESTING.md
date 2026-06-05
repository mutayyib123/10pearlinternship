# Unit Testing Guide

## 📋 Overview

This project includes comprehensive unit tests for:
- **Service Layer** - Business logic testing with Mockito
- **Controller Layer** - REST API endpoint testing with MockMvc
- **Repository Layer** - Database operations testing with Spring Data JPA
- **Integration Tests** - End-to-end workflow testing

---

## 🗂️ Test Files Structure

```
src/test/java/com/contactmanagementsystem/
├── service/
│   ├── UserServiceImplTest.java          (User service unit tests)
│   └── ContactServiceImplTest.java       (Contact service unit tests)
├── controller/
│   ├── UserControllerTest.java           (User API endpoint tests)
│   └── ContactControllerTest.java        (Contact API endpoint tests)
├── repository/
│   ├── UserRepositoryTest.java           (User repository tests)
│   └── ContactRepositoryTest.java        (Contact repository tests)
└── IntegrationTest.java                  (Full workflow integration tests)
```

---

## 📊 Test Coverage

| Layer | Class | Test Methods | Coverage |
|-------|-------|--------------|----------|
| **Service** | UserServiceImpl | 8 | Registration, Login, Change Password |
| **Service** | ContactServiceImpl | 10 | CRUD, Pagination, Search |
| **Controller** | UserController | 11 | Endpoints, Validation, Error Handling |
| **Controller** | ContactController | 11 | Endpoints, Pagination, Search |
| **Repository** | UserRepository | 8 | CRUD, Email Lookup |
| **Repository** | ContactRepository | 10 | CRUD, Search, Pagination |
| **Integration** | IntegrationTest | 4 | Full workflows |

**Total: 62 Test Methods** ✅

---

## 🚀 Running Tests

### Run All Tests
```bash
mvn test
```

### Run Tests in a Specific Class
```bash
mvn test -Dtest=UserServiceImplTest
mvn test -Dtest=ContactControllerTest
```

### Run Tests with Coverage Report
```bash
mvn test jacoco:report
```

### Run Tests in IDE
- **VS Code**: Use Test Explorer (Java Test Runner)
- **IntelliJ**: Click "Run" or right-click test class → "Run Tests"

---

## 🧪 Test Categories

### 1. **Service Layer Tests** (UserServiceImplTest, ContactServiceImplTest)

**Tested Using**: JUnit 5 + Mockito

**Example: User Registration Test**
```java
@Test
@DisplayName("Should register user successfully")
void testRegisterUserSuccess() {
    // Arrange - Set up mock data
    when(userRepository.existsByEmail(anyString())).thenReturn(false);
    when(passwordEncoder.encode(anyString())).thenReturn("$2a$10$hashedPassword");
    when(userRepository.save(any(User.class))).thenReturn(user);

    // Act - Call the method
    UserResponse response = userService.registerUser(registrationRequest);

    // Assert - Verify results
    assertNotNull(response);
    assertEquals("Ahmed Ali", response.getName());
    verify(userRepository, times(1)).existsByEmail("ahmed@example.com");
}
```

**Coverage**:
- ✅ User Registration (Success & Duplicate Email)
- ✅ User Login (Success & Invalid Password)
- ✅ Change Password (Success & Current Password Invalid)
- ✅ Contact CRUD Operations
- ✅ Pagination & Search

---

### 2. **Controller Layer Tests** (UserControllerTest, ContactControllerTest)

**Tested Using**: JUnit 5 + MockMvc + ObjectMapper

**Example: User Registration Endpoint Test**
```java
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
            .andExpect(jsonPath("$.email").value("ahmed@example.com"));
}
```

**Coverage**:
- ✅ All REST Endpoints (POST, PUT, DELETE, GET)
- ✅ Validation Error Handling (400 Bad Request)
- ✅ Response Status Codes (201, 200, 400, 404)
- ✅ JSON Response Structure

---

### 3. **Repository Layer Tests** (UserRepositoryTest, ContactRepositoryTest)

**Tested Using**: JUnit 5 + @DataJpaTest (No Spring Context)

**Example: User Repository Test**
```java
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
```

**Coverage**:
- ✅ CRUD Operations (Save, Find, Update, Delete)
- ✅ Custom Query Methods (findByEmail, existsByEmail)
- ✅ Search & Filter Operations
- ✅ Pagination

---

### 4. **Integration Tests** (IntegrationTest)

**Full End-to-End Workflow Tests**

```java
@Test
@DisplayName("Should complete full contact lifecycle")
void testContactLifecycleFlow() throws Exception {
    // 1. Register User
    // 2. Create Contact
    // 3. Get Contact
    // 4. Update Contact
    // 5. Search Contacts
    // 6. Delete Contact
    // 7. Verify Deletion
}
```

**Coverage**:
- ✅ User Registration → Contact Management workflow
- ✅ Validation Error Handling
- ✅ Unauthorized Access Handling
- ✅ Complete CRUD lifecycle

---

## 🔧 Test Technologies Used

| Tool | Purpose | Version |
|------|---------|---------|
| **JUnit 5** | Test Framework | Latest (Spring Boot) |
| **Mockito** | Mocking Framework | Latest (Spring Boot) |
| **MockMvc** | HTTP Testing | Latest (Spring Boot) |
| **@DataJpaTest** | Repository Testing | Spring Boot |
| **ObjectMapper** | JSON Serialization | Jackson |

---

## ✅ Test Examples

### Example 1: Service Test with Mocking
```java
@Test
@DisplayName("Should throw exception when email already registered")
void testRegisterUserEmailAlreadyExists() {
    when(userRepository.existsByEmail(anyString())).thenReturn(true);
    
    assertThrows(InvalidCredentialsException.class, () -> {
        userService.registerUser(registrationRequest);
    });
    
    verify(userRepository, never()).save(any(User.class));
}
```

### Example 2: Controller Test with Validation
```java
@Test
@DisplayName("Should return 400 when email is invalid")
void testRegisterUserInvalidEmail() throws Exception {
    registrationRequest.setEmail("invalid-email");
    
    mockMvc.perform(post("/api/users/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(registrationRequest)))
            .andExpect(status().isBadRequest());
}
```

### Example 3: Repository Test with Database
```java
@Test
@DisplayName("Should search case-insensitive")
void testSearchCaseInsensitive() {
    contactRepository.save(contact);
    
    Page<Contact> foundContacts = contactRepository
            .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                "ali", "ali", pageable);
    
    assertEquals(1, foundContacts.getTotalElements());
}
```

---

## 📈 Code Coverage

### Generate Coverage Report
```bash
mvn test jacoco:report
```

### View Report
Open `target/site/jacoco/index.html` in browser

### Coverage Goals
- **Service Layer**: 90%+
- **Controller Layer**: 85%+
- **Repository Layer**: 80%+
- **Overall**: 85%+

---

## 🐛 Debugging Tests

### Debug Mode
```bash
mvn -Dmaven.surefire.debug test
```

### View Test Output
```bash
mvn test -X
```

### Skip Tests During Build
```bash
mvn clean install -DskipTests
```

---

## 📝 Test Naming Convention

All tests follow this pattern:
```
test[MethodName][Scenario]
```

Examples:
- `testRegisterUserSuccess` - Happy path
- `testRegisterUserEmailAlreadyExists` - Error scenario
- `testDeleteContactSuccess` - Successful deletion
- `testFindContactNotFound` - Not found scenario

---

## 🎯 Best Practices

✅ **Do**:
- Use descriptive test names with `@DisplayName`
- Follow Arrange-Act-Assert pattern
- Mock external dependencies
- Test both success and error cases
- Keep tests isolated and independent
- Use `@BeforeEach` for common setup

❌ **Don't**:
- Mix multiple test scenarios in one test
- Create dependencies between tests
- Test implementation details
- Use real databases in unit tests
- Hard-code test data

---

## 🔗 Related Files

- `pom.xml` - Test dependencies configuration
- `application-test.properties` - Test configuration
- Test classes: See structure above

---

## 📞 Support

For test-related issues:
1. Check test output: `mvn test -X`
2. Review test class for setup errors
3. Ensure all dependencies are installed: `mvn clean install`
4. Clear Maven cache: `mvn clean`

