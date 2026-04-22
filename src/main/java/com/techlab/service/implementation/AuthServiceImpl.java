package com.techlab.service.implementation;

import com.techlab.dto.auth.AuthResponse;
import com.techlab.dto.auth.LoginRequest;
import com.techlab.dto.user.RegisterRequest;
import com.techlab.dto.user.UserDto;
import com.techlab.entity.User;
import com.techlab.exception.DuplicateUserException;
import com.techlab.exception.UserNotFoundException;
import com.techlab.mapper.UserMapper;
import com.techlab.repository.IUserRepository;
import com.techlab.service.IAuthService;
import com.techlab.service.IJwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service("authService")
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private final IUserRepository userRepository;
    private final IJwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;


    @Override
    public AuthResponse login(LoginRequest request) {
        // Authenticate using email
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        // Find user by email
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(UserNotFoundException::new);
        
        String token = jwtService.getToken(user);
        return AuthResponse.builder()
                .token(token)
                .build();
    }

    @Override
    public UserDto register(RegisterRequest request) {
        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())){
            throw new DuplicateUserException("Email already registered");
        }
        
        // Check if name already exists
        if (userRepository.existsByName(request.getName())){
            throw new DuplicateUserException("Username already exists");
        }
        
        User user = UserMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        return UserMapper.toUserDto(user);
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication==null || authentication.getPrincipal()==null){
            return null;
        }

        Long userId = (Long) authentication.getPrincipal();

        try{
            return userRepository.findById(userId).orElse(null);
        } catch (ClassCastException e){
            return null;
        }
    }

}