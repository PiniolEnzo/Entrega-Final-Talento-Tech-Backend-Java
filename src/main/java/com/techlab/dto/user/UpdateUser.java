package com.techlab.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
    @Size(min = 5, max = 25, message = "Name must be between 5 and 25 characters.")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$", message = "Name can only contain letters and spaces.")
    @Schema(description = "User's name (display name)", example = "Juan Pérez", requiredMode = Schema.RequiredMode.REQUIRED, minLength = 3, maxLength = 25)
    private String name;
}
