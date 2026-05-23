package com.duoc.team_service.models.dtos;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UsuarioDTO {

    private Long idUsuario;
    private String estado;
    private String correo;
    private String nickname;
    private String rol;

}
