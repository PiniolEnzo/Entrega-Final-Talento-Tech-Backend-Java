package com.techlab.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO for changing user password.
 * Contains the old password and the new password.
 */

@Data
@Schema(description = "Data required to change a user's password")
public class ChangePassword {
    @NotBlank(message = "Password is required.")
    @Size(min = 6, max = 25, message = "Password must be between 6 to 25 characters long.")
    @Schema(description = "Current password", example = "OldP@ss123", requiredMode = Schema.RequiredMode.REQUIRED, minLength = 6, maxLength = 25, format = "password")
    private String oldPassword;

    @NotBlank(message = "Password is required.")
    @Size(min = 6, max = 25, message = "Password must be between 6 to 25 characters long.")
    @Schema(description = "New password", example = "NewP@ss456", requiredMode = Schema.RequiredMode.REQUIRED, minLength = 6, maxLength = 25, format = "password")
    private String newPassword;

}
