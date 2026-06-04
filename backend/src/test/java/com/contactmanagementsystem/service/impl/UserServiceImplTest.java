package com.contactmanagementsystem.service.impl;

import com.contactmanagementsystem.dto.user.UserLoginRequest;
import com.contactmanagementsystem.dto.user.UserRegistrationRequest;
import com.contactmanagementsystem.entity.User;
import com.contactmanagementsystem.exception.InvalidCredentialsException;
import com.contactmanagementsystem.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void registerUser_success() {
        UserRegistrationRequest req = new UserRegistrationRequest();
        req.setName("Alice");
        req.setEmail("alice@example.com");
        req.setPassword("pass");

        when(userRepository.existsByEmail(req.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenAnswer(i -> {
            User u = i.getArgument(0);
            u.setId(1L);
            return u;
        });

        var res = userService.registerUser(req);
        assertNotNull(res);
        assertEquals(1L, res.getId());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerUser_existingEmail_throws() {
        UserRegistrationRequest req = new UserRegistrationRequest();
        req.setEmail("a@b.com");
        when(userRepository.existsByEmail(req.getEmail())).thenReturn(true);
        assertThrows(InvalidCredentialsException.class, () -> userService.registerUser(req));
    }

    @Test
    void loginUser_invalidPassword_throws() {
        UserLoginRequest req = new UserLoginRequest();
        req.setEmail("a@b.com");
        req.setPassword("wrong");

        User user = User.builder().id(1L).email(req.getEmail()).password("encoded").build();
        when(userRepository.findByEmail(req.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(eq(req.getPassword()), anyString())).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> userService.loginUser(req));
    }
}
