package com.duoc.notification_service.models.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class SancionDTO {

    private Long sancionId;
    private Long usuarioId;
    private Long equipoId;
}
