package com.example.ranking_service.models.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ResultDto {
    private Long resultadoId;
    private Long partidaId;
    private Long ganadorId;
    private Integer puntajeA;
    private Integer puntajeB;
    private String estadoValidacion;

}
