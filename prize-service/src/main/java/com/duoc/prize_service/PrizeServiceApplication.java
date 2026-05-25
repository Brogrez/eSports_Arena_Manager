package com.duoc.prize_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients // <--- ACTIVAR CLIENTES EN PAQUETE COM.DUOC
public class PrizeServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(PrizeServiceApplication.class, args);
	}
}