package com.duoc.prize_service.clients;

import com.duoc.prize_service.models.dtos.RankingDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ranking-service", url = "http://localhost:8008/api/v1/rankings")
public interface RankingClient {

    @GetMapping("/tournament/{torneoId}")
    RankingDTO findById(@PathVariable Long torneoId);
}