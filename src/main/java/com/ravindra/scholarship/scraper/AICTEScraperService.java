package com.ravindra.scholarship.scraper;

import com.ravindra.scholarship.model.Scholarship;
import com.ravindra.scholarship.repository.ScholarshipRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import javax.net.ssl.*;
import java.security.cert.X509Certificate;
import java.util.Optional;

@Service
public class AICTEScraperService {


    private final ScholarshipRepository repository;

    public AICTEScraperService(ScholarshipRepository repository) {
        this.repository = repository;
    }

    public void scrapeScholarships() {

        try {

            disableSSLVerification();

            Document doc = Jsoup.connect("https://www.aicte.gov.in/schemes/students-development-schemes")
                    .userAgent("Mozilla/5.0")
                    .timeout(10000)
                    .get();
//            Document doc = Jsoup.connect("https://www.mastersportal.com/scholarships/")
//                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 Chrome/120.0 Safari/537.36")
//                    .header("Accept-Language", "en-US,en;q=0.9")
//                    .header("Accept", "text/html")
//                    .timeout(10000)
//                    .get();

            Elements schemes = doc.select(".schemeListItem");

//            scholarships.forEach(element -> {
//
//                Scholarship s = new Scholarship();
//
//                s.setTitle(element.select(".scholarship-title").text());
//                s.setProvider(element.select(".scholarship-provider").text());
//                s.setAmount("Not specified");
//                s.setCategory("General");
//                s.setState("India");
//                s.setEligibility("Check website");
//                s.setApplicationLink(
//                        element.select("a").attr("href")
//                );
//
//                Optional<Scholarship> existing =
//                        repository.findByTitle(s.getTitle());
//
//                if(existing.isEmpty()){
//                    repository.save(s);
//                }
//
//            });

            for (Element scheme : schemes) {

                String title = scheme.select(".headingText").text();
                String description = scheme.select(".schemeDetailsText").text();

                if (title == null || title.isBlank()) {
                    continue;
                }

                Scholarship s = new Scholarship();
                s.setTitle(title);
                s.setProvider("AICTE");
                s.setAmount("Not specified");
                s.setCategory("Government");
                s.setState("India");
                s.setEligibility(description);
                s.setApplicationLink("https://www.aicte.gov.in/schemes/students-development-schemes");

                Optional<Scholarship> existing = repository.findByTitle(title);




                if (existing.isEmpty()) {
                    repository.save(s);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void disableSSLVerification() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() { return null; }
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                    }
            };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());

            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}