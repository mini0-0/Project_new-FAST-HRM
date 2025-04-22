package com.project.fasthrm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class FasthrmApplication {

	public static void main(String[] args) {
		SpringApplication.run(FasthrmApplication.class, args);
	}

}
