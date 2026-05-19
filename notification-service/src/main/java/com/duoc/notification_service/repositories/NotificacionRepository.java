package com.duoc.notification_service.repositories;

import com.duoc.notification_service.models.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion,Long> {
    List<Notificacion> findByUsuarioId(Long usuarioId);
    List<Notificacion> findByEquipoId(Long equipoId);

}
