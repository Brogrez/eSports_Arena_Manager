package com.duoc.registration_service.clients;

import com.duoc.registration_service.models.dtos.UsuarioDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "localhost:8000/api/v1/users")
public interface UsuarioClient {

    @GetMapping("/{id}")
    UsuarioDTO findById(@PathVariable Long id);

}
