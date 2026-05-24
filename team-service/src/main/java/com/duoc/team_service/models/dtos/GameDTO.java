package com.duoc.team_service.models.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor

public class GameDTO {

    private Long gameId;
    private String nombreJuego;
    private String genero;
    private String modalidad;
    private Integer jugadoresPorEquipo;
    private String estado;
}
