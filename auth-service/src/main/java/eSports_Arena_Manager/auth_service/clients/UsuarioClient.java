package eSports_Arena_Manager.auth_service.clients;

import eSports_Arena_Manager.auth_service.models.dtos.UsuarioDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "user-service", url = "localhost:8000/api/v1/users")
public interface UsuarioClient {


    @GetMapping("/{id}")
    UsuarioDTO findById(Long id);

}
