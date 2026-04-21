package com.techlab.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO for returning basic user information in API responses.
 * This class is used to expose a simplified view of the user entity.
 */

@Data
@Schema(description = "DTO for basic user response")
public class UserResponse {
    @Schema(description = "Unique identifier of the user", example = "1")
    private Long id;
    @Schema(description = "Username or login name", example = "john_doe")
    private String name;
}
