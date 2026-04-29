package com.techlab.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@Builder
@NoArgsConstructor @AllArgsConstructor
@Schema(description = "Represents a token used for resetting a user's password.")
@Table(name = "password_reset_tokens")
public class PasswordChangeToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Schema(description = "Unique identifier for the password reset token.", example = "1")
    private Long id;

    @Column(name = "token", nullable = false, unique = true)
    @Schema(description = "The unique token string used for password reset.", example = "abc123def456ghi789")
    private String token;

    @ManyToOne
    @Column(name = "user_id", nullable = false)
    @Schema(description = "The user associated with this password reset token.", example = "2")
    private User user;

    @Column(name = "expiration_date")
    @Schema(description = "The date and time when the token expires.", example = "2026-07-01T12:00:00")
    private LocalDateTime expirationDate;

    @Column(name = "used", nullable = false)
    @Schema(description = "Indicates whether the token has been used.", example = "false")
    private boolean used;

    @CreationTimestamp
    @Schema(description = "Timestamp when the product was created.", example = "2024-06-01T12:00:00")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Schema(description = "Timestamp when the product was last updated.", example = "2024-06-15T15:30:00")
    private LocalDateTime updatedAt;
}
