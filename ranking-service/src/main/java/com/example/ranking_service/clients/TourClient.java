package com.example.ranking_service.clients;

import com.example.ranking_service.models.dtos.TourDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "tournament-service", url = "http://localhost:8004/api/v1/tournaments")
public interface TourClient {
    @GetMapping("/{id}")
    TourDto findById(@PathVariable Long id);
}
