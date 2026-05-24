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

public class InscripcionDTO {

    private Long id;
    private Long torneoId;
    private Long equipoId;
    private Long usuarioId;
    private String tipoParticipante;
    private String estado;
    private LocalDateTime fechaInscripcion;
}
