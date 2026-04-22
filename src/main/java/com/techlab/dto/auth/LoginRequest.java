package com.techlab.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
    @Schema(description = "User's email", example = "juan@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @NotBlank(message = "Password is required.")
    @Size(min = 6, max = 25, message = "Password must be between 6 to 25 characters long.")
    @Schema(description = "User's password", example = "P@ssw0rd123", requiredMode = Schema.RequiredMode.REQUIRED, format = "password")
    private String password;
}