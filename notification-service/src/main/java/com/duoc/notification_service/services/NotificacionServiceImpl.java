package com.duoc.notification_service.services;

import com.duoc.notification_service.exceptions.NotificacionException;
import com.duoc.notification_service.models.Notificacion;
import com.duoc.notification_service.repositories.NotificacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NotificacionServiceImpl implements NotificacionService {

    @Autowired
    private NotificacionRepository notificacionRepository;

    @Transactional(readOnly = true)
    @Override
    public Notificacion findByUsuarioId(Long usuarioId) {
        return this.notificacionRepository.findByUsuarioId(usuarioId).orElseThrow(
                () -> new NotificacionException("Usuario no encontrado")
        );
    }

    @Transactional(readOnly = true)
    @Override
    public Notificacion findByEquipoId(Long equipoId){
        return this.notificacionRepository.findByEquipoId(equipoId).orElseThrow(
                () -> new NotificacionException("Equipo no encontrado")
        );

    }

    @Transactional(readOnly = true)
    @Override
    public List<Notificacion> findAll() {
        return this.notificacionRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Notificacion findBynotificacionId(Long notificacionId) {
        return this.notificacionRepository.findByNotificacionId(notificacionId).orElseThrow(
                () -> new NotificacionException("Notificacion no encontrado")
        );
    }

    @Transactional
    @Override
    public void deleteBynotificacionId(Long notificacionId) {
        this.notificacionRepository.deleteById(notificacionId);
    }

    @Override
    public Notificacion save(Notificacion notificacion) {
        return this.notificacionRepository.save(notificacion);
    }

    @Transactional
    @Override
    public Notificacion update(Long notificacionId,Notificacion notificacion) {
        return this.notificacionRepository.findByNotificacionId(notificacionId).map(n ->{
            n.setTipo(notificacion.getTipo());
            n.setEquipoId(notificacion.getEquipoId());
            n.setMensaje(notificacion.getMensaje());
            n.setUsuarioId(notificacion.getUsuarioId());
            n.setLeido(notificacion.isLeido());
            return this.notificacionRepository.save(n);
        }).orElseThrow(
                ()-> new NotificacionException("notificacion no encontrada")
        );
    }



    @Transactional
    @Override
    public Notificacion marcarComoLeido(Long id) {

        return this.notificacionRepository.findByNotificacionId(id).map(notif -> {
            notif.setLeido(true);
            return this.notificacionRepository.save(notif);

        }).orElseThrow(
                () -> new RuntimeException("Notificación no encontrada")
        );
    }
}
