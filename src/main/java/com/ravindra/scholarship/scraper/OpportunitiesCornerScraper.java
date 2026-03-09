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
public class OpportunitiesCornerScraper {

    private final ScholarshipRepository repository;

    public OpportunitiesCornerScraper(ScholarshipRepository repository) {
        this.repository = repository;
    }

    public void scrapeScholarships() {

        try {

            Document doc = Jsoup.connect("https://opportunitiescorners.com/scholarships/")
                    .userAgent("Mozilla/5.0")
                    .timeout(10000)
                    .get();

            Elements scholarships = doc.select("h2.entry-title a");

            for (Element element : scholarships) {

                String title = element.text();
                String link = element.attr("href");

                if (title == null || title.isBlank()) {
                    continue;
                }

                Scholarship s = new Scholarship();
                s.setTitle(title);
                s.setProvider("Opportunities Corner");
                s.setAmount("Not specified");
                s.setCategory("International");
                s.setState("Global");
                s.setEligibility("Check website");
                s.setApplicationLink(link);

                Optional<Scholarship> existing =
                        repository.findByTitle(title);

                if (existing.isEmpty()) {
                    repository.save(s);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}