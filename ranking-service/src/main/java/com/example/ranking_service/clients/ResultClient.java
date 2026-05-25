package com.example.ranking_service.clients;

import com.example.ranking_service.models.dtos.ResultDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "result-service", url = "http://localhost:8007/api/v1/results")
public interface ResultClient {
    @GetMapping("/{id}")
    ResultDto findById(@PathVariable Long id);
}
