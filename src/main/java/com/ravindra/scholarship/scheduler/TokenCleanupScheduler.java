package com.ravindra.scholarship.scheduler;

import com.ravindra.scholarship.repository.PasswordResetTokenRepository;
import com.ravindra.scholarship.repository.VerificationTokenRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class TokenCleanupScheduler {

    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final VerificationTokenRepository verificationTokenRepository;

    public TokenCleanupScheduler(
            PasswordResetTokenRepository passwordResetTokenRepository,
            VerificationTokenRepository verificationTokenRepository) {

        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.verificationTokenRepository = verificationTokenRepository;
    }

    @Scheduled(cron = "0 0 * * * *")
    public void cleanupExpiredTokens() {

        passwordResetTokenRepository.deleteAllExpired(Instant.now());
        verificationTokenRepository.deleteAllExpired(Instant.now());

        System.out.println("Expired tokens cleaned");
    }
}