package eSports_Arena_Manager.auth_service_login.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Datos que envia el cliente para iniciar sesion.
@Getter
@Setter
@NoArgsConstructor
public class LoginRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
