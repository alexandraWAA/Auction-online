package com.example.auctionapp;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition
public class AuctionAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuctionAppApplication.class, args);
	}

}
