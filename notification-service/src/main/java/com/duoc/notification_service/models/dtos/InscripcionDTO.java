package com.duoc.notification_service.models.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class InscripcionDTO {

    private Long inscripcionId;
    private Long equipoId;
    private Long jugadorId;
    private String tipoParticipante;
}
