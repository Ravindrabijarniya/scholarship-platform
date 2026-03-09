package com.ravindra.scholarship.service;

import com.ravindra.scholarship.dto.AuthResponse;
import com.ravindra.scholarship.dto.LoginRequest;
import com.ravindra.scholarship.dto.RegisterRequest;
import com.ravindra.scholarship.model.RefreshToken;
import com.ravindra.scholarship.model.Role;
import com.ravindra.scholarship.model.User;
import com.ravindra.scholarship.repository.UserRepository;
import com.ravindra.scholarship.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder encoder;
    private final RefreshTokenService refreshTokenService;

    public AuthService(UserRepository userRepository,
                       JwtProvider jwtProvider,
                       PasswordEncoder encoder, RefreshTokenService refreshTokenService) {

        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
        this.encoder = encoder;
        this.refreshTokenService = refreshTokenService;
    }

    public void register(RegisterRequest request){

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setRole(Role.valueOf(request.getRole()));

        userRepository.save(user);
    }

    public AuthResponse login(LoginRequest request){

        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow();

        if(!encoder.matches(request.getPassword(), user.getPassword())){
            throw new RuntimeException("Invalid credentials");
        }

        String accessToken = jwtProvider.generateToken(user);

        RefreshToken refreshToken =
                refreshTokenService.createRefreshToken(user);

        AuthResponse response = new AuthResponse();

        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken.getToken());

        return response;
    }
}