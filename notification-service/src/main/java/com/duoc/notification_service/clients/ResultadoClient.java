package com.duoc.notification_service.clients;

import com.duoc.notification_service.models.dtos.ResultadoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "result-service", url = "localhost:8007/api/v1/results")
public interface ResultadoClient {

    @GetMapping("/{id}")
    ResultadoDTO findbyid(@PathVariable Long id);

}
