package com.ravindra.scholarship.repository;

import com.ravindra.scholarship.model.VerificationToken;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    Optional<VerificationToken> findByToken(String token);

    @Transactional
    @Modifying
    @Query("DELETE FROM VerificationToken t WHERE t.expiryDate < :now")
    void deleteAllExpired(Instant now);
}
