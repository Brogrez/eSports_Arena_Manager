package com.duoc.notification_service.clients;

import com.duoc.notification_service.models.dtos.SancionDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "sanction-service", url = "localhost:8009/api/v1/sanctions")
public interface SancionClient {

    @GetMapping("/{id}")
    SancionDTO findbyId(@PathVariable Long id);;

}
