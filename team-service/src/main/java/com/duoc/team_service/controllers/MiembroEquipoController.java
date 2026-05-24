package com.duoc.team_service.controllers;

import com.duoc.team_service.models.MiembroEquipo;
import com.duoc.team_service.models.dtos.MiembroEquipoDTO;
import com.duoc.team_service.services.MiembroEquipoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/miembros_equipo")

public class MiembroEquipoController {

    @Autowired
    private MiembroEquipoService miembroEquipoService;

    @GetMapping
    public ResponseEntity<List<MiembroEquipoDTO>> findAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(miembroEquipoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MiembroEquipo> getById(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(miembroEquipoService.findBymEquipoId(id));
    }

    @GetMapping("/equipo/{mEquipoId}")
    public ResponseEntity<MiembroEquipo> findByMEquipoId(@PathVariable Long mEquipoId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(miembroEquipoService.findBymEquipoId(mEquipoId));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<MiembroEquipo>>findByUsuarioId(@PathVariable Long usuarioId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(miembroEquipoService.findByUsuarioId(usuarioId));
    }

     @GetMapping("/rol/{rolDentroEquipo}")
    public ResponseEntity<MiembroEquipo> findByRolDentroEquipo(@PathVariable String rolDentroEquipo) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(miembroEquipoService.findByRolDentroEquipo(rolDentroEquipo));
    }
}


