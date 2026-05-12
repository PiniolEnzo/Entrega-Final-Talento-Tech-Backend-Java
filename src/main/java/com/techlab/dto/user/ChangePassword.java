package com.techlab.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO for changing user password when already authenticated.
 * Contains the old password and the new password only.
 * For the forgot-password flow, use {@link com.techlab.dto.auth.ResetPasswordRequest}.
 */

@Data
@Schema(description = "Data required for an authenticated user to change their password")
public class ChangePassword {
    @NotBlank(message = "Old password is required.")
    @Size(min = 10, max = 25, message = "Password must be between 10 to 25 characters long.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{10,}$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character.")
    @Schema(description = "Current password", example = "OldP4ss123-", requiredMode = Schema.RequiredMode.REQUIRED, minLength = 10, maxLength = 25, format = "password")
    private String oldPassword;

    @NotBlank(message = "New password is required.")
    @Size(min = 10, max = 25, message = "Password must be between 10 to 25 characters long.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{10,}$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character.")
    @Schema(description = "New password", example = "NewP4ss456-", requiredMode = Schema.RequiredMode.REQUIRED, minLength = 10, maxLength = 25, format = "password")
    private String newPassword;
}
