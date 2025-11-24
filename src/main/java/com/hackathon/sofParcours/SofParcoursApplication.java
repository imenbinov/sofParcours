package com.hackathon.sofParcours;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SofParcoursApplication {

	public static void main(String[] args) {
		SpringApplication.run(SofParcoursApplication.class, args);
	}

}
