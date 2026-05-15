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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HexFormat;
import java.util.List;
import java.util.UUID;

import static com.techlab.utils.UserAccessValidate.validateUserAccess;


@Slf4j
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
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(UserNotFoundException::new);

        String token = jwtService.getToken(user);
        return AuthResponse.builder()
                .token(token)
                .build();
    }

    @Override
    public UserDto register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())){
            throw new DuplicateUserException("Email already registered");
        }

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

        Long userId = jwtService.extractUserId(token);
        if (userId == null) {
            throw new BadCredentialsException("Invalid token");
        }

        validateUserAccess(userId);

        Long expiration = jwtService.getUserIdFromToken(token);
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
        if (authentication == null || authentication.getPrincipal() == null){
            return null;
        }

        Long userId = (Long) authentication.getPrincipal();

        return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public void createPasswordResetToken(String email) {
        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(UserNotFoundException::new);

            // 1. Invalidar tokens anteriores del mismo usuario
            List<PasswordChangeToken> tokensAnteriores =
                    passwordResetTokenRepository.findByUserAndUsedFalse(user);
            tokensAnteriores.forEach(t -> t.setUsed(true));
            passwordResetTokenRepository.saveAll(tokensAnteriores);

            // 2. Generar token UUID y hashearlo (SHA-256)
            String rawToken = UUID.randomUUID().toString();
            String hashedToken = hashToken(rawToken);

            // 3. Mandar el email PRIMERO (con el token original)
            emailService.sendPasswordResetEmail(email, user.getName(), rawToken);

            // 4. Si el email se mandó bien, recién persistimos el token hasheado
            passwordResetTokenRepository.save(PasswordChangeToken.builder()
                    .token(hashedToken)
                    .user(user)
                    .expirationDate(LocalDateTime.now().plusMinutes(10))
                    .used(false)
                    .build()
            );

        } catch (UserNotFoundException e) {
            // No revelar si el email existe o no — siempre responder 200
            log.warn("Solicitud de reset para email inexistente: {}", email);
        }
    }

    @Override
    public PasswordChangeToken validatePasswordResetToken(String rawToken) {
        String hashedToken = hashToken(rawToken);

        PasswordChangeToken resetToken = passwordResetTokenRepository.findByToken(hashedToken)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));

        if (resetToken.isUsed() || resetToken.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Token is expired or already used");
        }

        return resetToken;
    }

    @Override
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BadCredentialsException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public void resetPassword(String rawToken, String newPassword) {
        PasswordChangeToken resetToken = validatePasswordResetToken(rawToken);

        User user = resetToken.getUser();

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        passwordResetTokenRepository.delete(resetToken);
    }

    // ========================================================================
    // Utils
    // ========================================================================

    /**
     * Hashea un token con SHA-256 para no guardarlo en texto plano en la BD.
     * El hash es determinístico, lo que permite buscar por token en la BD.
     */
    private String hashToken(String rawToken) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(rawToken.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }


}