package com.techlab.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
@AllArgsConstructor
@Schema(description = "Request payload for initiating a password reset process.")
public class ForgotPasswordRequest {
    @Email
    @NotBlank
    @Schema(description = "The email address of the user requesting a password reset.", example = "joan@gmail.com")
    private String email;
}
