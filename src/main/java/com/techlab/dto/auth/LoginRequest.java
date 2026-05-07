package com.techlab.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@Schema(description = "Login credentials")
public class LoginRequest {
    @NotBlank(message = "Email is required.")
    @Email(message = "Invalid email format.")
    @Size(max = 60, message = "Email must be at most 60 characters long.")
    @Schema(description = "User's email", example = "juan@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @NotBlank(message = "Password is required.")
    @Size(min = 10, max = 25, message = "Password must be between 10 to 25 characters long.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^\\w\\s'\"<>`])[^\\s'\"<>`]{10,25}$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character.")
    @Schema(description = "User's password", example = "Password1-", requiredMode = Schema.RequiredMode.REQUIRED, minLength = 6, maxLength = 25, format = "password")
    private String password;
}