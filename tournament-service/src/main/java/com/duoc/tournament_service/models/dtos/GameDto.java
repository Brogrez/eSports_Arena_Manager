package com.duoc.tournament_service.models.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class GameDto {
    private Long gameId;
    private String name;
    private String estado;
    private String modalidad;
    private Integer jugadoresPorEquipo;
}
