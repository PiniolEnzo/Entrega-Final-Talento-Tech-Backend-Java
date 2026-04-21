package com.techlab.controller;


import com.techlab.dto.auth.AuthResponse;
import com.techlab.dto.auth.LoginRequest;
import com.techlab.dto.user.RegisterRequest;
import com.techlab.dto.user.UserDto;
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
    @PostMapping(value = "login")
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
    @PostMapping(value = "register")
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
    @PostMapping(value = "logout")
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


}