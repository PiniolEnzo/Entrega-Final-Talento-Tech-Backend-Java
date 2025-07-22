package com.techlab.service;

import com.techlab.dto.auth.AuthResponse;
import com.techlab.dto.auth.LoginRequest;
import com.techlab.dto.user.RegisterRequest;
import com.techlab.dto.user.UserDto;
import com.techlab.entity.User;

public interface IAuthService {

    AuthResponse login(LoginRequest request);

    UserDto register(RegisterRequest request);

    User getCurrentUser();
}
