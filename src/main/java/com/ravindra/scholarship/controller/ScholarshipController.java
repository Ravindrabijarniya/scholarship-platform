package com.ravindra.scholarship.controller;

import com.ravindra.scholarship.model.Scholarship;
import com.ravindra.scholarship.scraper.AICTEScraperService;
import com.ravindra.scholarship.service.ScholarshipAggregatorService;
import com.ravindra.scholarship.service.ScholarshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/scholarships")
@RequiredArgsConstructor
public class ScholarshipController {

    private final ScholarshipService service;

    @Autowired
    private AICTEScraperService scraperService;

    @Autowired
    private ScholarshipAggregatorService aggregatorService;

    @PostMapping
    public Scholarship create(@RequestBody Scholarship scholarship) {
        return service.createScholarship(scholarship);
    }

//    @GetMapping
//    public List<Scholarship> getAll() {
//        return service.getAllScholarships();
//    }

    @GetMapping("/{id}")
    public Scholarship getById(@PathVariable Long id) {
        return service.getScholarship(id);
    }

    @GetMapping
    public List<Scholarship> searchScholarships(
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String category
    ) {
        return service.search(state, category);
    }

    @PostMapping("/scrape")
    @PreAuthorize("hasRole('ADMIN')")
    public String scrape() {

        aggregatorService.scrapeAll();

        return "All scrapers executed";
    }

    @GetMapping("/search")
    public List<Scholarship> searchScholarships(

            @RequestParam(required = false) String course,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) Integer income

    ) {

        return service.filterScholarships(course,state,gender,income);
    }

    @GetMapping("/upcoming")
    public List<Scholarship> upcomingScholarships(){

        return service.findByDeadlineAfter(LocalDate.now());
    }
}