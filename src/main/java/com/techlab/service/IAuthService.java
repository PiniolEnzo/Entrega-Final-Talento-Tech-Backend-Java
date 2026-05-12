package com.techlab.service;

import com.techlab.dto.auth.AuthResponse;
import com.techlab.dto.auth.LoginRequest;
import com.techlab.dto.user.RegisterRequest;
import com.techlab.dto.user.UserDto;
import com.techlab.dto.user.UserProfileResponse;
import com.techlab.entity.PasswordChangeToken;
import com.techlab.entity.User;
import jakarta.mail.MessagingException;

public interface IAuthService {

    AuthResponse login(LoginRequest request);

    UserDto register(RegisterRequest request);

    User getCurrentUser();

    void createPasswordResetToken(String email);

    void changePassword(Long userId, String oldPassword, String newPassword);

    void resetPassword(String token, String newPassword);

    PasswordChangeToken validatePasswordResetToken(String token);

    void logout(String authHeader);

    UserProfileResponse getUserProfile(Long userId);

}
