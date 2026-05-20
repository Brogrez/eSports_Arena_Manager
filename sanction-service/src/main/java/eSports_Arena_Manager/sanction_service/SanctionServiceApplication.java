package eSports_Arena_Manager.sanction_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class SanctionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SanctionServiceApplication.class, args);
	}

}
