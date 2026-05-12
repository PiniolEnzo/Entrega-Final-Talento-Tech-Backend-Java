package com.techlab.repository;

import com.techlab.entity.PasswordChangeToken;
import com.techlab.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("passwordResetTokenRepository")
public interface IPasswordResetTokenRepository extends JpaRepository<PasswordChangeToken, Long> {
    Optional<PasswordChangeToken> findByToken(String token);

    List<PasswordChangeToken> findByUserAndUsedFalse(User user);
}
