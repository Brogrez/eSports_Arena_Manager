package com.duoc.team_service.controllers;

import com.duoc.team_service.models.MiembroEquipo;
import com.duoc.team_service.models.dtos.MiembroEquipoDTO;
import com.duoc.team_service.services.MiembroEquipoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ResponseEntity<MiembroEquipo>save(@Valid @RequestBody MiembroEquipo miembroEquipo) {
        return ResponseEntity.status(HttpStatus.CREATED).body(miembroEquipoService.save(miembroEquipo));
    }


    @PutMapping("/{id}")
    public ResponseEntity<MiembroEquipo> update(@PathVariable Long id, @Valid @ RequestBody MiembroEquipo miembroEquipo) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(miembroEquipoService.update(id, miembroEquipo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        miembroEquipoService.deleteByMiembroId(miembroEquipoService.findBymEquipoId(id));
        return ResponseEntity.noContent().build();
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


