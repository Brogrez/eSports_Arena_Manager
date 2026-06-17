package eSports_Arena_Manager.auth_service_login.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

// Vista segura de un usuario (sin la contrasena).
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    private Long usuarioId;
    private String username;
    private Set<String> roles;
}
