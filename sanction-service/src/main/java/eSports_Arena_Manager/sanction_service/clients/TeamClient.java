package eSports_Arena_Manager.sanction_service.clients;

import eSports_Arena_Manager.sanction_service.models.dtos.TeamDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "team-service", url = "http://localhost:8003/api/v1/teams")
public interface TeamClient {
    @GetMapping("/{id}")
    TeamDto findById(@PathVariable Long id);
}

