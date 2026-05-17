package com.duoc.tournament_service.clients;


import com.duoc.tournament_service.models.dtos.GameDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "game-service", url = "http://localhost:8002/api/v1/games")
public interface GameClient {
    @GetMapping("/{id}")
    GameDto findByid(@PathVariable Long id);
}
