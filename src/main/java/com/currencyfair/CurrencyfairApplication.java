package com.currencyfair;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class CurrencyfairApplication {

	public static void main(String[] args) {
		SpringApplication.run(CurrencyfairApplication.class, args);
	}

}
