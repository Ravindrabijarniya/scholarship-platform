package com.ravindra.scholarship.scheduler;

import com.ravindra.scholarship.service.ScholarshipAggregatorService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@EnableScheduling
public class ScraperScheduler {

    private final ScholarshipAggregatorService service;

    public ScraperScheduler(ScholarshipAggregatorService service) {
        this.service = service;
    }

    @Scheduled(cron = "0 0 3 * * *")
    public void runScraper() {

        System.out.println("Running scholarship scraper...");

        service.scrapeAll();
    }
}
