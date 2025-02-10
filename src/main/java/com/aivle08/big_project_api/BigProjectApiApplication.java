package com.aivle08.big_project_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class BigProjectApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BigProjectApiApplication.class, args);
	}

}
