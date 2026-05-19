package com.duoc.notification_service.repositories;

import com.duoc.notification_service.models.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion,Long> {
    Optional<Notificacion> findByUsuarioId(Long usuarioId);
    Optional<Notificacion> findByEquipoId(Long equipoId);
    Optional<Notificacion> findByNotificacionId(Long id);
}
