package com.ravindra.scholarship.controller;

import com.ravindra.scholarship.dto.*;
import com.ravindra.scholarship.model.PasswordResetToken;
import com.ravindra.scholarship.model.RefreshToken;
import com.ravindra.scholarship.model.User;
import com.ravindra.scholarship.model.VerificationToken;
import com.ravindra.scholarship.repository.PasswordResetTokenRepository;
import com.ravindra.scholarship.repository.RefreshTokenRepository;
import com.ravindra.scholarship.repository.UserRepository;
import com.ravindra.scholarship.repository.VerificationTokenRepository;
import com.ravindra.scholarship.security.JwtProvider;
import com.ravindra.scholarship.service.AuthService;
import com.ravindra.scholarship.service.EmailService;
import com.ravindra.scholarship.service.RefreshTokenService;
import com.ravindra.scholarship.service.TokenBlacklistService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    private final JwtProvider jwtProvider;

    private final TokenBlacklistService tokenBlacklistService;

    private final RefreshTokenService refreshTokenService;

    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
//    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;


    public AuthController(AuthService authService, UserRepository userRepository, RefreshTokenRepository refreshTokenRepository, JwtProvider jwtProvider, TokenBlacklistService tokenBlacklistService, RefreshTokenService refreshTokenService, VerificationTokenRepository verificationTokenRepository, PasswordResetTokenRepository passwordResetTokenRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.authService = authService;
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtProvider = jwtProvider;
        this.tokenBlacklistService = tokenBlacklistService;
        this.refreshTokenService = refreshTokenService;
        this.verificationTokenRepository = verificationTokenRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request){

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEnabled(false);

        userRepository.save(user);

        // 🔥 EMAIL VERIFICATION TOKEN
        String token = UUID.randomUUID().toString();

        VerificationToken vt = new VerificationToken();
        vt.setToken(token);
        vt.setUser(user);
        vt.setExpiryDate(Instant.now().plus(1, ChronoUnit.DAYS));

        verificationTokenRepository.save(vt);

        return "Registration successful. Please verify your email.";
    }

    @PostMapping("/login")
    public AuthResponse  login(@RequestBody LoginRequest request){

        return authService.login(request);
    }

//    @PostMapping("/refresh")
//    public AuthResponse refreshToken(@RequestBody RefreshRequest request){
//
//        String token = request.getRefreshToken();
//
//        RefreshToken refreshToken =
//                refreshTokenRepository.findByToken(token)
//                        .orElseThrow(() -> new RuntimeException("Invalid refresh token"));
//
//        String newAccessToken =
//                jwtProvider.generateToken(refreshToken.getUser());
//
//        return new AuthResponse(newAccessToken, token);
//    }

    @PostMapping("/refresh")
    public AuthResponse refreshToken(@RequestBody RefreshRequest request) {

        RefreshToken refreshToken = refreshTokenService
                .findByToken(request.getRefreshToken())
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        refreshTokenService.verifyExpiration(refreshToken);

        User user = refreshToken.getUser();

        // 🔥 delete old refresh token
        refreshTokenService.delete(refreshToken);

        // 🔥 create new refresh token
        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user);

        String accessToken = jwtProvider.generateToken(user);

        return new AuthResponse(accessToken, newRefreshToken.getToken());
    }

//    @PostMapping("/logout")
//    public ResponseEntity<?> logout(HttpServletRequest request) {
//
//        String token = jwtProvider.getJwtFromHeader(request);
//
//        if(token == null)
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token missing");
//
//        long expiration = jwtProvider.getRemainingExpiration(token);
//
//        tokenBlacklistService.blacklistToken(token, expiration);

//        return ResponseEntity.ok("Logged out successfully");
//    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {

        String token = jwtProvider.getJwtFromHeader(request);

        long expiration = jwtProvider.getRemainingExpiration(token);

        tokenBlacklistService.blacklistToken(token, expiration);

        return ResponseEntity.ok("Logged out successfully");
    }

    @GetMapping("/verify")
    public String verifyAccount(@RequestParam String token) {

        VerificationToken vt = verificationTokenRepository
                .findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        User user = vt.getUser();
        user.setEnabled(true);

        userRepository.save(user);

        return "Account verified successfully";
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestBody EmailRequest request){

        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = UUID.randomUUID().toString();

        PasswordResetToken prt = new PasswordResetToken();
        prt.setToken(token);
        prt.setUser(user);
        prt.setExpiryDate(Instant.now().plus(30, ChronoUnit.MINUTES));

        passwordResetTokenRepository.save(prt);

        emailService.sendResetLink(user.getEmail(), token);

        return "Reset link sent";
    }


    @PostMapping("/reset-password")
    public String resetPassword(@RequestBody ResetPasswordRequest request){

        PasswordResetToken prt = passwordResetTokenRepository
                .findByToken(request.getToken())
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if(prt.getExpiryDate().isBefore(Instant.now())){
            throw new RuntimeException("Token expired");
        }

        User user = prt.getUser();

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        userRepository.save(user);

        return "Password updated successfully";
    }
}