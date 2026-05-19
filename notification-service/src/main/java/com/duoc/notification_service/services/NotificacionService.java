package com.duoc.notification_service.services;

import com.duoc.notification_service.models.Notificacion;

import java.util.List;

public interface NotificacionService {

    Notificacion findByUsuarioId(Long usuarioId);
    Notificacion findByEquipoId(Long equipoId);
    List<Notificacion> findAll();
    Notificacion findBynotificacionId(Long id);
    void deleteBynotificacionId(Long id);
    Notificacion save(Notificacion notificacion);
    Notificacion update(Notificacion notificacion);
    Notificacion marcarComoLeido(Long id);
}
