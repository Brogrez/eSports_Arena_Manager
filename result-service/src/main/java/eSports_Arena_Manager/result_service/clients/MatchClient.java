package eSports_Arena_Manager.result_service.clients;

import eSports_Arena_Manager.result_service.models.dtos.MatchDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "match-service", url = "http://localhost:8082/api/v1/matches")
public interface MatchClient {
    @GetMapping("/{id}")
    MatchDTO findById(@PathVariable("id") Long id);
}
