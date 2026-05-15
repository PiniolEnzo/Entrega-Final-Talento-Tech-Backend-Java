package com.techlab.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Request payload for resetting a forgotten password using a reset token.")
public class ResetPasswordRequest {

    @NotBlank(message = "Token is required.")
    @Schema(description = "Password reset token received by email", example = "550e8400-e29b-41d4-a716-446655440000", requiredMode = Schema.RequiredMode.REQUIRED)
    private String token;

    @NotBlank(message = "New password is required.")
    @Size(min = 10, max = 25, message = "Password must be between 10 to 25 characters long.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&._-])[A-Za-z\\d@$!%*?&._-]{10,25}$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character (.@$!%*?&._-)")
    @Schema(description = "New password", example = "NewP4ss456-", requiredMode = Schema.RequiredMode.REQUIRED, minLength = 10, maxLength = 25, format = "password")
    private String newPassword;
}
