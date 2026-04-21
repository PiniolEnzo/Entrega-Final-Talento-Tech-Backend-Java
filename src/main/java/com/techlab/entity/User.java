package com.techlab.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a user in the system.
 * Implements {@link UserDetails} for Spring Security integration.
 */

@Getter @Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Represents a user of the system")
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @EqualsAndHashCode.Include
    @Schema(description = "Unique identifier of the user", example = "1")
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    @Schema(description = "Username used for login", example = "johndoe")
    private String name;

    @Column(name = "password", nullable = false)
    @Schema(description = "Encrypted user password", example = "$2a$10$...")
    private String password;

    @Column(name = "active")
    @Schema(description = "Indicates whether the user is active", example = "true")
    private boolean active;

    @CreationTimestamp
    @Schema(description = "Timestamp when the user was created")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Schema(description = "Timestamp when the user was last updated")
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    @Schema(description = "Role assigned to the user", example = "ADMIN")
    private Role userRole;

    /**
     * Constructs a new User with the specified name, password, and active status.
     *
     * @param name     The username
     * @param password The encrypted password
     * @param active   Whether the user is active
     */
    public User(String name, String password, boolean active) {
        this.name = name;
        this.password = password;
        this.active = active;
    }

/**
     * Constructs a new User with the specified name, password, active status, and role.
     *
     * @param name      The username
     * @param password  The encrypted password
     * @param active    Whether the user is active
     * @param userRole  The role assigned to the user
     */
    public User(String name, String password, boolean active, Role userRole) {
        this.name = name;
        this.password = password;
        this.active = active;
        this.userRole = userRole;
    }

    /**
     * Returns a collection of granted authorities for the user.
     * This method is used by Spring Security to determine the user's roles and permissions.
     *
     * @return A collection of granted authorities, in this case, a single authority based on the user's role
     * @see GrantedAuthority
     * @see UserDetails#getAuthorities()
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Spring Security's hasRole() adds "ROLE_" prefix automatically
        // So we need to include "ROLE_" prefix for hasRole() to work correctly
        return List.of(new SimpleGrantedAuthority("ROLE_" + userRole.name()));
    }
    /**
     * Returns the name of the user.
     * This method is used by Spring Security for authentication.
     *
     * @return The name of the user
     */
    @Override
    public String getUsername() {
        return this.name;
    }
}
