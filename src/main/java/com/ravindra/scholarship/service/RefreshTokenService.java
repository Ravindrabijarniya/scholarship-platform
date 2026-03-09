package com.ravindra.scholarship.service;

import com.ravindra.scholarship.model.RefreshToken;
import com.ravindra.scholarship.model.User;
import com.ravindra.scholarship.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final long REFRESH_TOKEN_DURATION = 7 * 24 * 60 * 60 * 1000;

    @Autowired
    RefreshTokenRepository repository;

    public RefreshToken createRefreshToken(User user){

        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(
                Instant.now().plusMillis(REFRESH_TOKEN_DURATION)
        );

        return repository.save(refreshToken);
    }

    public void delete(RefreshToken token){
        repository.delete(token);
    }

    public Optional<RefreshToken> findByToken(String token){
        return repository.findByToken(token);
    }

    // ⏳ check expiration
    public void verifyExpiration(RefreshToken token){

        if(token.getExpiryDate().isBefore(Instant.now())){
            repository.delete(token);
            throw new RuntimeException("Refresh token expired. Please login again.");
        }

    }

}
