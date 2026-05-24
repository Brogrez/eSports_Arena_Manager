package eSports_Arena_Manager.auth_service.clients;

import eSports_Arena_Manager.auth_service.models.dtos.UsuarioDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "http://localhost:8000/api/v1/users")
public interface UsuarioClient {

    @GetMapping("/{id}")
    UsuarioDTO findById(@PathVariable Long id);

}
