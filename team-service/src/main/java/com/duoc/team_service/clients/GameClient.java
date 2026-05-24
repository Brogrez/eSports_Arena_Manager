package com.duoc.team_service.clients;

import com.duoc.team_service.models.dtos.GameDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="game-service", url = "localhost:8002/api/v1/games")
public interface GameClient {

    @GetMapping("/{id}")
    GameDTO findById(@PathVariable Long id);

}
