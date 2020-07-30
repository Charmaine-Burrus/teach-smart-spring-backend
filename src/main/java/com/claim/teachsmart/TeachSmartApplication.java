package com.claim.teachsmart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@ComponentScan(basePackages="com.claim")
@EnableJpaRepositories("com.claim.repository")
public class TeachSmartApplication {

	public static void main(String[] args) {
		SpringApplication.run(TeachSmartApplication.class, args);
	}

}
