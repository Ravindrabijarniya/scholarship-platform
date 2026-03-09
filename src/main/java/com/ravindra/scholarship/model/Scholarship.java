package com.ravindra.scholarship.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Scholarship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String provider;

    private String amount;

    private String category;

    private String state;

    private LocalDate deadline;

    @Column(length = 3000)
    private String eligibility;

    private String applicationLink;

    private String course;
    private String gender;
    private Integer minIncome;
    private Integer maxIncome;
}