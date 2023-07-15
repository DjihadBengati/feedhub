package com.db.feedhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class FeedhubApplication {

	public static void main(String[] args) {
		SpringApplication.run(FeedhubApplication.class, args);
	}

}
