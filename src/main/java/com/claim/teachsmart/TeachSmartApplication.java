package com.claim.teachsmart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages="com.claim")
public class TeachSmartApplication {

	public static void main(String[] args) {
		SpringApplication.run(TeachSmartApplication.class, args);
	}

}
