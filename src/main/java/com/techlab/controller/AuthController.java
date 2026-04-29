package com.techlab.controller;


import com.techlab.dto.auth.AuthResponse;
import com.techlab.dto.auth.ForgotPasswordRequest;
import com.techlab.dto.auth.LoginRequest;
import com.techlab.dto.user.ChangePassword;
import com.techlab.dto.user.RegisterRequest;
import com.techlab.dto.user.UserDto;
import com.techlab.dto.user.UserProfileResponse;
import com.techlab.service.IAuthService;
import com.techlab.service.IJwtService;
import com.techlab.service.ILogoutService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user authentication and registration")
public class AuthController {

    private final IAuthService authService;
    private final ILogoutService logoutService;
    private final IJwtService jwtService;

    @Operation(
            summary = "User Login",
            description = "Authenticate a user and return an authentication token"
    )
    @ApiResponses(
            value ={
                    @ApiResponse(responseCode = "200", description = "Successful login",
                        content = @Content(schema = @Schema(implementation = AuthResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid login request"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - invalid credentials")
            }
    )
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Login request payload",
                    required = true,
                    content = @Content(schema = @Schema(implementation = LoginRequest.class))
            )
            @RequestBody @Valid LoginRequest request){
        return ResponseEntity.ok(authService.login(request));
    }


    @Operation(
            summary = "User Registration",
            description = "Register a new user and return the created user details"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Successful registration",
                            content = @Content(schema = @Schema(implementation = UserDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid registration request"),
                    @ApiResponse(responseCode = "409", description = "Conflict in registration")
            }
    )
    @PostMapping("/register")
    public ResponseEntity<UserDto> register(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Registration request payload",
                    required = true,
                    content = @Content(schema = @Schema(implementation = RegisterRequest.class))
            )
                                                @RequestBody @Valid RegisterRequest request){
        return ResponseEntity.ok(authService.register(request));
    }

    @Operation(
            summary = "User Logout",
            description = "Invalidate the current JWT token to log out the user"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Successfully logged out"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - no token provided")
            }
    )
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader) {
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).build();
        }
        
        String token = authHeader.substring(7);
        
        // Get token expiration time
        Long userId = jwtService.extractUserId(token);
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }
        
        // Get expiration from token
        Long expiration = jwtService.getUserIdFromToken(token);
        // Note: We need to get the actual expiration time from the token
        // Let's use a method to get expiration
        java.util.Date expirationDate = jwtService.getTokenExpiration(token);
        if (expirationDate != null) {
            logoutService.invalidateToken(token, expirationDate.getTime());
        }
        
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Get Current User Profile",
            description = "Get profile data of the currently authenticated user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User profile retrieved successfully",
                    content = @Content(schema = @Schema(implementation = UserProfileResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - no token provided"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/me")
    public ResponseEntity<?> me() {
        try {
            // Get current user ID from security context
            Long userId = null;
            var authentication = org.springframework.security.core.context.SecurityContextHolder
                    .getContext().getAuthentication();
            
            if (authentication != null && authentication.getPrincipal() instanceof Long) {
                userId = (Long) authentication.getPrincipal();
            }
            
            if (userId == null) {
                return ResponseEntity.status(401).body(Map.of("error", "Not authenticated"));
            }
            
            // Get user data from service
            var user = authService.getCurrentUser();
            if (user == null) {
                return ResponseEntity.status(404).body(Map.of("error", "User not found"));
            }
            
            // Build profile response (no sensitive data)
            UserProfileResponse profile = com.techlab.dto.user.UserProfileResponse.builder()
                    .name(user.getName())
                    .email(user.getEmail())
                    .build();
            
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Internal error: " + e.getMessage()));
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        authService.createPasswordResetToken(request.getEmail());
        return ResponseEntity.ok().body(Map.of("message", "If an account with that email exists, a password reset link has been sent."));
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestParam String token) {
        authService.validatePasswordResetToken(token);
        return ResponseEntity.ok().body(Map.of("message", "Token is valid"));
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePassword request) {
        authService.changePassword(request.getOldPassword(), request.getNewPassword(), request.getToken());
        return ResponseEntity.ok().body(Map.of("message", "Password changed successfully"));
    }


}