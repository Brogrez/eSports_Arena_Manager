package com.duoc.notification_service.services;

import com.duoc.notification_service.models.Notificacion;

import java.util.List;

public interface NotificacionService {
    List<Notificacion> findByUsuarioId(Long usuarioId);
    List<Notificacion> findByEquipoId(Long equipoId);
    List<Notificacion> findAll();
    Notificacion findBynotificacionId(Long id);

}
