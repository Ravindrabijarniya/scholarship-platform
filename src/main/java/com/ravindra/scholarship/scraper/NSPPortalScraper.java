package com.ravindra.scholarship.scraper;

import com.ravindra.scholarship.model.Scholarship;
import com.ravindra.scholarship.repository.ScholarshipRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NSPPortalScraper {

    private final ScholarshipRepository repository;

    public NSPPortalScraper(ScholarshipRepository repository) {
        this.repository = repository;
    }

    public void scrapeScholarships() {

        try {

            Document doc = Jsoup.connect("https://scholarships.gov.in/")
                    .userAgent("Mozilla/5.0")
                    .timeout(10000)
                    .get();

            Elements scholarships = doc.select("a");

            for (Element element : scholarships) {

                String title = element.text();

                if (title.contains("Scholarship")) {

                    Optional<Scholarship> existing =
                            repository.findByTitle(title);

                    if (existing.isEmpty()) {

                        Scholarship s = new Scholarship();
                        s.setTitle(title);
                        s.setProvider("National Scholarship Portal");
                        s.setAmount("Not specified");
                        s.setCategory("Government");
                        s.setState("India");
                        s.setEligibility("Check website");
                        s.setApplicationLink("https://scholarships.gov.in/");

                        repository.save(s);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}