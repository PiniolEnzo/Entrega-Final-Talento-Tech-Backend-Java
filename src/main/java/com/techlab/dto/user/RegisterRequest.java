package com.techlab.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * DTO for user registration.
 * Contains the necessary data to create a new user.
 */

@Data
@Builder
@AllArgsConstructor
@Schema(description = "Data for registering a new user")
public class RegisterRequest {
    @NotBlank(message = "Name is required.")
    @Size(min = 5, max = 25, message = "Description must be between 5 and 25 characters.")
    @Schema(description = "User's name", example = "exampleUser", requiredMode = Schema.RequiredMode.REQUIRED, minLength = 5, maxLength = 25)
    private String name;

    @NotBlank(message = "Password is required.")
    @Size(min = 6, max = 25, message = "Password must be between 6 to 25 characters long.")
    @Schema(description = "User's password", example = "P@ssw0rd123", requiredMode = Schema.RequiredMode.REQUIRED, minLength = 6, maxLength = 25, format = "password")
    private String password;
}
