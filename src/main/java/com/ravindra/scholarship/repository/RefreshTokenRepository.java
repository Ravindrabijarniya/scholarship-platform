package com.ravindra.scholarship.repository;

import com.ravindra.scholarship.model.RefreshToken;
import com.ravindra.scholarship.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository
        extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    void deleteByUser(User user);

}
