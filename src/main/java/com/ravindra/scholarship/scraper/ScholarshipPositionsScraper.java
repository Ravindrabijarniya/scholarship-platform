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
public class ScholarshipPositionsScraper {

    private final ScholarshipRepository repository;

    public ScholarshipPositionsScraper(ScholarshipRepository repository) {
        this.repository = repository;
    }

    public void scrapeScholarships() {

        try {

            for(int page = 1; page <= 5; page++) {

                String url;

                if(page == 1){
                    url = "https://scholarship-positions.com/";
                } else {
                    url = "https://scholarship-positions.com/page/" + page + "/";
                }

                System.out.println("Scraping: " + url);

                Document doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0")
                        .timeout(10000)
                        .get();

                Elements scholarships = doc.select("h3.entry-title a");

                for (Element element : scholarships) {

                    String title = element.text();
                    String link = element.attr("href");

                    if (title == null || title.isBlank()) continue;

                    Optional<Scholarship> existing =
                            repository.findByTitle(title);

                    if(existing.isEmpty()){

                        Scholarship s = new Scholarship();

                        s.setTitle(title);
                        s.setProvider("Scholarship Positions");
                        s.setAmount("Not specified");
                        s.setCategory("International");
                        s.setState("Global");
                        s.setEligibility("Check website");
                        s.setApplicationLink(link);

                        repository.save(s);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}