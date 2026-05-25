package com.duoc.registration_service.models.dtos;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor

public class SancionDTO {

    private Long sancionId;
    private Long usuarioId;
    private Long equipoId;
    private String motivo;
    private String estado;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private String severidad;

}
