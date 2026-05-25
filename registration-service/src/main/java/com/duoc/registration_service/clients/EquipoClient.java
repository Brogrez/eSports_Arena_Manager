package com.duoc.registration_service.clients;

import com.duoc.registration_service.models.dtos.EquipoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "team-service", url = "localhost:8003/api/v1/equipos")
public interface EquipoClient {

    @GetMapping("/{id}")
    EquipoDTO findById(@PathVariable Long id);

}
