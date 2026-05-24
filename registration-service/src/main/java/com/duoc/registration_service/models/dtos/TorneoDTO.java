package com.duoc.registration_service.models.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor

public class TorneoDTO {

    private Long torneoId;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private Integer cupoMaximo;
    private String estado;
    private String modalidad;
}
