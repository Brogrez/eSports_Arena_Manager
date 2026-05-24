package com.duoc.notification_service.clients;

import com.duoc.notification_service.models.dtos.InscripcionDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "registration-service", url = "localhost:8007/api/v1/inscripciones")
public interface RegistroClient {

    @GetMapping("/{id}")
    InscripcionDTO findbyId(@PathVariable Long id);;
}
