package com.duoc.registration_service.models.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor

public class TorneoDTO {

    private Long torneoId;
    private Date fechaInicio;
    private Date fechaFin;
    private Integer cupoMaximo;
    private String estado;
    private String modalidad;
}
