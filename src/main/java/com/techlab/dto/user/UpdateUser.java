package com.techlab.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO used to update user information.
 * Includes fields that are editable by the user.
 */

@Data
@Schema(description = "DTO used to update an existing user's data")
public class UpdateUser {
    @NotBlank(message = "Name is required.")
    @Size(min = 5, max = 25, message = "Description must be between 5 and 25 characters.")
    @Schema(description = "Updated name of the user", example = "Travis Scott", minLength = 5, maxLength = 25, requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
}
