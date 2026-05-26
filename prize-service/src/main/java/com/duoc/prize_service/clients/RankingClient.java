package com.duoc.prize_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ranking-service", url = "http://localhost:8083/api/v1/rankings")
public interface RankingClient {

    @GetMapping("/tournament/{torneoId}")
    Object findByTorneoId(@PathVariable("torneoId") Long torneoId);
}