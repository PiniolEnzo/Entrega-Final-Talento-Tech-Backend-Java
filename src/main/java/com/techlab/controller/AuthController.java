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
import io.swagger.v3.oas.annotations.Parameter;
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
            description = "Authenticate a user and return an authentication token",
                parameters = {
                        @Parameter(name = "request", description = "Login request payload containing email and password", required = true)
                }
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
            description = "Register a new user and return the created user details",
            parameters = {
                    @Parameter(name = "request", description = "Registration request payload containing user details", required = true)
            }
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
            description = "Invalidate the current JWT token to log out the user",
            parameters = {
                    @Parameter(name = "authHeader", description = "JWT token in the format 'Bearer {token}'", required = true)
            }
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Successfully logged out"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - no token provided"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
            }
    )
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader) {
        authService.logout(authHeader);
        return ResponseEntity.noContent().build();
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
        return ResponseEntity.ok(authService.getUserProfile(authService.getCurrentUser().getId()));
    }


    @Operation(
            summary = "Forgot Password",
            description = "Initiate the password reset process by providing the user's email",
            parameters = {
                    @Parameter(name = "request", description = "Forgot password request payload containing the user's email", required = true)
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password reset initiated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid email format"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        authService.createPasswordResetToken(request.getEmail());
        return ResponseEntity.ok().body(Map.of("message", "If an account with that email exists, a password reset link has been sent."));
    }

    @Operation(
            summary = "Validate Password Reset Token",
            description = "Validate the password reset token to ensure it's valid and not expired",
            parameters = {
                    @Parameter(name = "token", description = "Password reset token to validate", required = true)
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token is valid"),
            @ApiResponse(responseCode = "400", description = "Invalid or expired token"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestParam String token) {
        authService.validatePasswordResetToken(token);
        return ResponseEntity.ok().body(Map.of("message", "Token is valid"));
    }

    @Operation(
            summary = "Change Password",
            description = "Change the user's password using the old password and a valid reset token",
            parameters = {
                    @Parameter(name = "request", description = "Change password request payload containing old password, new password, and reset token", required = true)
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password changed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request - incorrect old password or invalid/expired token"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePassword request) {
        authService.changePassword(request.getOldPassword(), request.getNewPassword(), request.getToken());
        return ResponseEntity.ok().body(Map.of("message", "Password changed successfully"));
    }


}