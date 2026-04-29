package com.techlab.repository;

import com.techlab.entity.PasswordChangeToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("passwordResetTokenRepository")
public interface IPasswordResetTokenRepository extends JpaRepository<PasswordChangeToken, Long> {
    Optional<PasswordChangeToken> findByToken(String token);
}
