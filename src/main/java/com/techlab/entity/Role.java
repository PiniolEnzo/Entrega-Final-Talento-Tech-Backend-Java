package com.techlab.entity;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Enum representing the different roles a user can have in the system.
 * <p>
 * Roles are typically used to control access and permissions within the application.
 * This enum is often stored in the database as a string via {@code @Enumerated(EnumType.STRING)}
 * and associated with the {@link User} entity.
 * <p>
 * Common roles include:
 * <dl>
 *     <dt>- USER</dt>
 *     <dd>Role with controlled access and restricted permissions</dd>
 *     <dt>- ADMIN</dt>
 *     <dd>Role with full administrative privileges</dd>
 * </dl>
 * <p>
 *
 */
@Schema(description = "Enum representing user roles within the system.")
public enum Role {
    /**
     * Role with controlled access and restricted permissions.
     */
@Schema(description = "User role with controlled access and restricted permissions.")
    USER,
    /**
     * Role with full administrative privileges.
     */
    @Schema(description = "Administrator role with full access.")
    ADMIN
}
