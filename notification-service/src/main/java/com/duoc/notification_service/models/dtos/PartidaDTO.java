package com.duoc.notification_service.models.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class PartidaDTO {
    private Long partidaId;
    private String participanteA;
    private String participanteB;
}
