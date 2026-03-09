package com.ravindra.scholarship.repository;

import com.ravindra.scholarship.model.Scholarship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ScholarshipRepository extends JpaRepository<Scholarship, Long> {

    List<Scholarship> findByState(String state);

    List<Scholarship> findByCategory(String category);

    List<Scholarship> findByStateAndCategory(String state, String category);

    Optional<Scholarship> findByTitle(String title);

    List<Scholarship> findByCourse(String course);

    List<Scholarship> findByGender(String gender);

    List<Scholarship> findByDeadlineIsNotNullAndDeadlineAfter(LocalDate date);

    List<Scholarship> findByDeadlineIsNotNullOrderByDeadlineAsc();
}