package com.duoc.prize_service.clients;

import com.duoc.prize_service.models.dtos.TournamentDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "tournament-service", url = "http://localhost:8004/api/v1/tournaments")
public interface TournamentClient {

    @GetMapping("/{id}")
    TournamentDTO findById(@PathVariable Long id);
}