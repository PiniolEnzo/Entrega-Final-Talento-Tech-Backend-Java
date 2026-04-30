package com.techlab.service.implementation;

import com.techlab.dto.auth.AuthResponse;
import com.techlab.dto.auth.LoginRequest;
import com.techlab.dto.user.RegisterRequest;
import com.techlab.dto.user.UserDto;
import com.techlab.dto.user.UserProfileResponse;
import com.techlab.entity.PasswordChangeToken;
import com.techlab.entity.User;
import com.techlab.exception.DuplicateUserException;
import com.techlab.exception.UserNotFoundException;
import com.techlab.mapper.UserMapper;
import com.techlab.repository.IPasswordResetTokenRepository;
import com.techlab.repository.IUserRepository;
import com.techlab.service.IAuthService;
import com.techlab.service.IEmailService;
import com.techlab.service.IJwtService;
import com.techlab.service.ILogoutService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

import static com.techlab.utils.UserAccessValidate.validateUserAccess;


@Service("authService")
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private final IUserRepository userRepository;
    private final IPasswordResetTokenRepository passwordResetTokenRepository;
    private final IJwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final IEmailService emailService;
    private final ILogoutService logoutService;


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

    @Override
    public void logout(String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new InsufficientAuthenticationException("Invalid Authorization header");
        }

        String token = authHeader.substring(7);

        // Get token expiration time
        Long userId = jwtService.extractUserId(token);
        if (userId == null) {
            throw new BadCredentialsException("Invalid token");
        }

        validateUserAccess(userId);

        // Get expiration from token
        Long expiration = jwtService.getUserIdFromToken(token);
        // Note: We need to get the actual expiration time from the token
        // Let's use a method to get expiration
        Date expirationDate = jwtService.getTokenExpiration(token);
        if (expirationDate != null) {
            logoutService.invalidateToken(token, expirationDate.getTime());
        }
    }

    @Override
    public UserProfileResponse getUserProfile(Long userId) {

        validateUserAccess(userId);

        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        return UserProfileResponse.builder()
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }


    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication==null || authentication.getPrincipal()==null){
            return null;
        }

        Long userId = (Long) authentication.getPrincipal();

        return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public void createPasswordResetToken(String email) throws MessagingException {
        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);

        validateUserAccess(user.getId());

        String token = UUID.randomUUID().toString();

        passwordResetTokenRepository.save(PasswordChangeToken.builder()
                .token(token)
                .user(user)
                .expirationDate(LocalDateTime.now().plusMinutes(10))
                .used(false)
                .build()
        );

        emailService.sendPasswordResetEmail(email, token);
    }

    @Override
    public PasswordChangeToken validatePasswordResetToken(String token) {
        PasswordChangeToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token")); //token not found

        if (resetToken.isUsed() || resetToken.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Token is expired or already used"); //token expired or already used
        }

        validateUserAccess(resetToken.getUser().getId());

        return resetToken;
    }

    @Override
    public void changePassword(String oldPassword, String newPassword, String token) {
        PasswordChangeToken resetToken = validatePasswordResetToken(token);

        User user = resetToken.getUser();

        validateUserAccess(user.getId());

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect"); //old password does not match (Bad Credentials)
        }



        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        passwordResetTokenRepository.delete(resetToken);
    }


}