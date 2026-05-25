package eSports_Arena_Manager.result_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
@SpringBootApplication
@EnableFeignClients

public class ResultServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(ResultServiceApplication.class, args);
	}
}
