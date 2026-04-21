package com.techlab.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @NotBlank(message = "Name is required.")
    @Size(min = 5, max = 25, message = "Description must be between 5 and 25 characters.")
    @Schema(description = "User's name or email", example = "exampleUser", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotBlank(message = "Password is required.")
    @Size(min = 6, max = 25, message = "Password must be between 6 to 25 characters long.")
    @Schema(description = "User's password", example = "P@ssw0rd123", requiredMode = Schema.RequiredMode.REQUIRED, format = "password")
    private String password;
}

