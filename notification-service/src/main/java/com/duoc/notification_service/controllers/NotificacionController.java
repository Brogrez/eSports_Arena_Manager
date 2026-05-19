package com.duoc.notification_service.controllers;

import com.duoc.notification_service.models.Notificacion;
import com.duoc.notification_service.services.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/notificaciones")

public class NotificacionController {

    @Autowired
    private NotificacionService notificacionService;

    @GetMapping
    public ResponseEntity<List<Notificacion>> findAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(notificacionService.findAll());
    }

    @GetMapping("/{notificacionId}")
    public ResponseEntity<Notificacion>findByNotificacionId(@PathVariable Long notificacionId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(notificacionService.findBynotificacionId(notificacionId));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<Notificacion> findByUsuarioId(@PathVariable Long usuarioId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(notificacionService.findByUsuarioId(usuarioId));
    }

    @GetMapping("/equipo/{equipoId}")
    public ResponseEntity<Notificacion> findByEquipoId(@PathVariable Long equipoId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(notificacionService.findByEquipoId(equipoId));
    }

    @PostMapping
    public ResponseEntity<Notificacion> save(@RequestBody Notificacion notificacion) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(notificacionService.save(notificacion));
    }

    @PutMapping("/{notificacionId}")
    public ResponseEntity<Notificacion>update(@PathVariable Long notificacionId, @RequestBody Notificacion notificacion) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(notificacionService.update(notificacionId, notificacion));
    }

    @DeleteMapping
    public ResponseEntity<Notificacion> delete(@PathVariable Long notificacionId) {
        notificacionService.deleteBynotificacionId(notificacionId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
