package com.ravindra.scholarship;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ScholarshipPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScholarshipPlatformApplication.class, args);
	}

}
