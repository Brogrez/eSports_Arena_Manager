package com.example.match_service.clients;

import com.example.match_service.models.dtos.TourDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public class TourClient {
    @FeignClient(name = "tournament-service", url = "http://localhost:8004/api/v1/tournaments")
    public interface TournamentClient {
        @GetMapping("/{id}")
        TourDto findById(@PathVariable Long id);
    }
}
