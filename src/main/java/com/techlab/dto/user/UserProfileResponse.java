package com.techlab.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * DTO for current user profile response.
 * Contains non-sensitive data for frontend profile view.
 */
@Data
@Builder
@Schema(description = "User profile data for frontend")
public class UserProfileResponse {
    
    @Schema(description = "User ID", example = "1")
    private Long id;
    
    @Schema(description = "User's display name", example = "Juan Pérez")
    private String name;
    
    @Schema(description = "User's email", example = "juan@example.com")
    private String email;
    
    @Schema(description = "User role", example = "USER")
    private String role;
    
    @Schema(description = "Is user active", example = "true")
    private Boolean active;
}