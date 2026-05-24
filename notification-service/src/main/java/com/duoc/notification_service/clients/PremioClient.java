package com.duoc.notification_service.clients;

import com.duoc.notification_service.models.dtos.PremioDTO;
import com.duoc.notification_service.models.dtos.ResultadoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "prize-service", url = "localhost:8010/api/v1/prizes")
public interface PremioClient {

    @GetMapping("/{id}")
    PremioDTO findbyId(@PathVariable Long id);;
}
