package com.duoc.team_service.models.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MiembroEquipoDTO {

    private Long miembroId;
    private String rolEnEquipo;
    private String estado;
    private Long equipoId;
    private Long UsuarioId;

}
