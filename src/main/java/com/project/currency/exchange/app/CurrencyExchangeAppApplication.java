package com.project.currency.exchange.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class CurrencyExchangeAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(CurrencyExchangeAppApplication.class, args);
	}

}
