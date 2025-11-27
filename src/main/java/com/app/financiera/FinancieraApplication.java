package com.app.financiera;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class FinancieraApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinancieraApplication.class, args);
	}

}
