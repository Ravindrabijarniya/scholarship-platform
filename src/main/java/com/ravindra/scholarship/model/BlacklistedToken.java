package com.ravindra.scholarship.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class BlacklistedToken {

    @Id
    @GeneratedValue
    private Long id;

    private String token;
}
