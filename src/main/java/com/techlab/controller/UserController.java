package com.techlab.controller;

import com.techlab.dto.auth.AuthResponse;
import com.techlab.dto.user.*;
import com.techlab.mapper.UserMapper;
import com.techlab.service.IAuthService;
import com.techlab.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Tag(name = "Users", description = "Operations for user management")
public class UserController {
    private final IUserService userService;

    /**
     * Get current user ID from security context
     */
    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof Long) {
            return (Long) auth.getPrincipal();
        }
        return null;
    }

    /**
     * Check if current user is admin
     */
    private boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            return auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        }
        return false;
    }

    /**
     * Validate that user can access the resource: either own profile or admin
     */
    private void validateUserAccess(Long targetUserId) {
        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            throw new AccessDeniedException("Authentication required");
        }
        // Admin can access all, USER can only access their own profile
        if (!isAdmin() && !targetUserId.equals(currentUserId)) {
            throw new AccessDeniedException("No tienes acceso a este usuario");
        }
    }

    @Operation(
            summary = "Get all users (ADMIN)",
            description = "Retrieve a list of all users in the system. Requires JWT authentication with ADMIN role."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval",
                    content = @Content(schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token missing or invalid"),
            @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN role")
    })
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers(){
        // Already protected by SecurityConfig (hasRole ADMIN)
        return ResponseEntity.ok().body(userService.findAll());
    }

    @Operation(
            summary = "Get user by ID",
            description = "Retrieve a specific user by their unique identifier. Users can only view their own profile."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access denied - can only view own profile"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id){
        validateUserAccess(id);
        return ResponseEntity.ok().body(userService.findById(id));
    }


    @Operation(
            summary = "Update user",
            description = "Update an existing user's information. Users can only update their own profile."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request - invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access denied - can only update own profile"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable(name = "id") Long id,
            @Valid @RequestBody UpdateUser request
            ){
        validateUserAccess(id);
        return ResponseEntity.ok().body(userService.update(id, request));
    }

    @Operation(
            summary = "Delete user (ADMIN)",
            description = "Delete a user by their unique identifier. Requires JWT authentication with ADMIN role."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN role"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        // Already protected by SecurityConfig (hasRole ADMIN)
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Change user password",
            description = "Change the password for a specific user. Users can only change their own password."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Password changed successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request - current password is incorrect"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access denied - can only change own password"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/{id}/change-password")
    public ResponseEntity<Void> changePassword (
            @PathVariable Long id,
            @Valid @RequestBody ChangePassword request){

        validateUserAccess(id);
        userService.changePassword(id, request);
        return ResponseEntity.noContent().build();
    }

}