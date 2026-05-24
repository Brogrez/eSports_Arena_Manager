package com.duoc.registration_service.clients;

import com.duoc.registration_service.models.dtos.TorneoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "tournament-service", url = "localhost:8004/api/v1/tournaments")
public interface TorneoClient {

    @GetMapping
    TorneoDTO findById(@PathVariable Long id);
}
