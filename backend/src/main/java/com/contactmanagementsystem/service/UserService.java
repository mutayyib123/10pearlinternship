package com.contactmanagementsystem.service;

import com.contactmanagementsystem.dto.user.AuthResponse;
import com.contactmanagementsystem.dto.user.ChangePasswordRequest;
import com.contactmanagementsystem.dto.user.UserLoginRequest;
import com.contactmanagementsystem.dto.user.UserRegistrationRequest;
import com.contactmanagementsystem.dto.user.UserResponse;

public interface UserService {

    UserResponse registerUser(UserRegistrationRequest request);

    AuthResponse loginUser(UserLoginRequest request);

    void changePassword(ChangePasswordRequest request);
}