package com.duoc.notification_service.clients;

import com.duoc.notification_service.models.dtos.PartidaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "match-service", url = "localhost:8006/api/v1/matches")
public interface PartidaClient {

    @GetMapping("/{id}")
    PartidaDTO findbyid(@PathVariable Long id);
}
