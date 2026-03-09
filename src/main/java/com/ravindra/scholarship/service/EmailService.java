package com.ravindra.scholarship.service;

import org.springframework.stereotype.Service;

@Service
public class EmailService {

    public void sendResetLink(String email, String token) {

        String link = "http://localhost:8080/auth/reset-password?token=" + token;

        System.out.println("Send email to: " + email);
        System.out.println("Reset link: " + link);
    }
}