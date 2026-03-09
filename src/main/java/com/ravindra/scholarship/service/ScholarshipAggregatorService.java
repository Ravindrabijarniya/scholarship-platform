package com.ravindra.scholarship.service;

import com.ravindra.scholarship.scraper.AICTEScraperService;
import com.ravindra.scholarship.scraper.NSPPortalScraper;
import com.ravindra.scholarship.scraper.OpportunitiesCornerScraper;
import com.ravindra.scholarship.scraper.ScholarshipPositionsScraper;
import org.springframework.stereotype.Service;

@Service
public class ScholarshipAggregatorService {

    private final AICTEScraperService aicte;
    private final ScholarshipPositionsScraper positions;
    private final OpportunitiesCornerScraper opportunities;
    private final NSPPortalScraper nsp;

    public ScholarshipAggregatorService(
            AICTEScraperService aicte,
            ScholarshipPositionsScraper positions,
            OpportunitiesCornerScraper opportunities,
            NSPPortalScraper nsp) {

        this.aicte = aicte;
        this.positions = positions;
        this.opportunities = opportunities;
        this.nsp = nsp;
    }

    public void scrapeAll() {

        System.out.println("Running AICTE scraper...");
        aicte.scrapeScholarships();

        System.out.println("Running ScholarshipPositions scraper...");
        positions.scrapeScholarships();

        System.out.println("Running OpportunitiesCorner scraper...");
        opportunities.scrapeScholarships();

        System.out.println("Running NSP scraper...");
        nsp.scrapeScholarships();
    }
}