package com.example.match_service.clients;

import com.example.match_service.models.dtos.RegistrationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public class RegistrationClients {
    @FeignClient(name = "registration-service", url = "http://localhost:8005/api/v1/inscripciones")
    public interface RegistrationClient {
        @GetMapping("/{id}")
        RegistrationDto findById(@PathVariable Long id);
    }
}
