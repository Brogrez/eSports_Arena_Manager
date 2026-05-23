package com.duoc.team_service.models.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class EquipoDTO {

    private Long equipoId;
    private String nombreEquipo;
    private String juegoPrincipal;
    private String estado;
    private Long juegoPrincipalId;
    private Long capitanId;

}
