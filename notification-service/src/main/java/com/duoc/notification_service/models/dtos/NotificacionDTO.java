package com.duoc.notification_service.models.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class NotificacionDTO {
    private Long notificacionId;
    private Long usuarioId;
    private Long equipoId;
    private String mensaje;
    private String tipo;
    private boolean leido;
}
