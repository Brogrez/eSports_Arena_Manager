package com.duoc.tournament_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class TournamentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TournamentServiceApplication.class, args);
	}

}
