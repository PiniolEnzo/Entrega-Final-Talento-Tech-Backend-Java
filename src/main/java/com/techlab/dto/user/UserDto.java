package com.techlab.dto.user;

import com.techlab.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Full Data Transfer Object (DTO) for User entity.
 * Used to transfer complete user data between layers or in API responses.
 */

@Data
@Schema(description = "Full DTO for user data transfer")
public class UserDto {
    @Schema(description = "Unique identifier of the user", example = "1")
    private Long id;
    @Schema(description = "User's display name", example = "Juan Pérez")
    private String name;
    @Schema(description = "User's email (login)", example = "juan@example.com")
    private String email;
    @Schema(description = "Whether the user is active", example = "true")
    private Boolean active;
    @Schema(description = "Timestamp when the user was created")
    private LocalDateTime createdAt;
    @Schema(description = "Timestamp when the user was last updated")
    private LocalDateTime updatedAt;
    @Schema(description = "Role assigned to the user", example = "ADMIN")
    private Role userRole;

}
