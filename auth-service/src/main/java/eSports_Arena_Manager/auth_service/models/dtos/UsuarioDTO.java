package eSports_Arena_Manager.auth_service.models.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UsuarioDTO {

    private Long userId;
    private String rol;
    private String estado;
    private LocalDate fechaRegistro;
    private String correo;
    private String nickname;

}
