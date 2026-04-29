package com.techlab.controller;

import com.techlab.dto.user.*;
import com.techlab.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.techlab.utils.UserAccessValidate.validateUserAccess;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Tag(name = "Users", description = "Operations for user management")
public class UserController {
    private final IUserService userService;

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
}