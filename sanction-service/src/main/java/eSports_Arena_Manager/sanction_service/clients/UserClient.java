package eSports_Arena_Manager.sanction_service.clients;

import eSports_Arena_Manager.sanction_service.models.dtos.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "http://localhoys:8000/api/v1/users")
public interface UserClient {
    @GetMapping("/{id}")
    UserDto findById(@PathVariable Long id);
}
