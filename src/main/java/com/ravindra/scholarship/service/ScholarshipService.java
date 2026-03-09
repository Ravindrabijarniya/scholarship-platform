//package com.ravindra.scholarship.service;
//
//import com.ravindra.scholarship.model.Scholarship;
//import com.ravindra.scholarship.repository.ScholarshipRepository;
//import org.springframework.cache.annotation.Cacheable;
//import org.springframework.cache.annotation.CacheEvict;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class ScholarshipService {
//
//    private final ScholarshipRepository repository;
//
//    public Scholarship createScholarship(Scholarship scholarship) {
//        return repository.save(scholarship);
//    }
//
//    @Cacheable(value = "scholarships")
//    public List<Scholarship> getAllScholarships() {
//        return repository.findAll();
//    }
//
//    public Scholarship getScholarship(Long id) {
//        return repository.findById(id).orElseThrow();
//    }
//
//    public List<Scholarship> search(String state, String category) {
//
//        if (state != null && category != null) {
//            return repository.findByStateAndCategory(state, category);
//        }
//
//        if (state != null) {
//            return repository.findByState(state);
//        }
//
//        if (category != null) {
//            return repository.findByCategory(category);
//        }
//
//        return repository.findAll();
//    }
//
//    public List<Scholarship> filterScholarships(
//            String course,
//            String state,
//            String gender,
//            Integer income
//    ) {
//
//        return repository.findAll()
//                .stream()
//                .filter(s -> course == null || course.equalsIgnoreCase(s.getCourse()))
//                .filter(s -> state == null || state.equalsIgnoreCase(s.getState()))
//                .filter(s -> gender == null || gender.equalsIgnoreCase(s.getGender()))
//                .filter(s -> income == null ||
//                        (s.getMaxIncome() == null || income <= s.getMaxIncome()))
//                .toList();
//    }
//
//    public List<Scholarship> findByDeadlineAfter(LocalDate date) {
//        return repository.findByDeadlineIsNotNullAndDeadlineAfter(date);
//    }
//
//    public List<Scholarship> getScholarshipsByNearestDeadline() {
//        return repository.findByDeadlineIsNotNullOrderByDeadlineAsc();
//    }
//
//}

package com.ravindra.scholarship.service;

import com.ravindra.scholarship.model.Scholarship;
import com.ravindra.scholarship.repository.ScholarshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScholarshipService {

    private final ScholarshipRepository repository;

    // clear cache when new scholarship is created
    @CacheEvict(value = {"scholarships", "searchScholarships", "upcomingScholarships"}, allEntries = true)
    public Scholarship createScholarship(Scholarship scholarship) {
        return repository.save(scholarship);
    }

    // cache all scholarships
    @Cacheable(value = "scholarships")
    public List<Scholarship> getAllScholarships() {
        return repository.findAll();
    }

    // cache by id
    @Cacheable(value = "scholarship", key = "#id")
    public Scholarship getScholarship(Long id) {
        return repository.findById(id).orElseThrow();
    }

    // cache search results
    @Cacheable(value = "searchScholarships", key = "#state + '-' + #category")
    public List<Scholarship> search(String state, String category) {

        if (state != null && category != null) {
            return repository.findByStateAndCategory(state, category);
        }

        if (state != null) {
            return repository.findByState(state);
        }

        if (category != null) {
            return repository.findByCategory(category);
        }

        return repository.findAll();
    }

    // cache filters
    @Cacheable(value = "filterScholarships", key = "#course + '-' + #state + '-' + #gender + '-' + #income")
    public List<Scholarship> filterScholarships(
            String course,
            String state,
            String gender,
            Integer income
    ) {

        return repository.findAll()
                .stream()
                .filter(s -> course == null || course.equalsIgnoreCase(s.getCourse()))
                .filter(s -> state == null || state.equalsIgnoreCase(s.getState()))
                .filter(s -> gender == null || gender.equalsIgnoreCase(s.getGender()))
                .filter(s -> income == null ||
                        (s.getMaxIncome() == null || income <= s.getMaxIncome()))
                .toList();
    }

    // cache upcoming scholarships
    @Cacheable(value = "upcomingScholarships")
    public List<Scholarship> findByDeadlineAfter(LocalDate date) {
        return repository.findByDeadlineIsNotNullAndDeadlineAfter(date);
    }

    public List<Scholarship> getScholarshipsByNearestDeadline() {
        return repository.findByDeadlineIsNotNullOrderByDeadlineAsc();
    }
}