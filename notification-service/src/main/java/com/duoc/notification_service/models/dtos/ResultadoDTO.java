package com.duoc.notification_service.models.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ResultadoDTO {
    private Long resultadoId;
    private Long partidaId;
    private String participanteA;
    private String participanteB;
    private String ganadorId;
}
