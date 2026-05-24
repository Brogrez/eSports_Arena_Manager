package com.duoc.notification_service.services;

import com.duoc.notification_service.models.Notificacion;
import com.duoc.notification_service.models.dtos.NotificacionDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface NotificacionService {

    Notificacion findByUsuarioId(Long usuarioId);
    Notificacion findByEquipoId(Long equipoId);
    List<NotificacionDTO> findAll();
    Notificacion findBynotificacionId(Long id);
    void deleteBynotificacionId(Long id);
    Notificacion save(Notificacion notificacion);
    Notificacion update(Long notificacionId,Notificacion notificacion);
    Notificacion marcarComoLeido(Long id);
}
