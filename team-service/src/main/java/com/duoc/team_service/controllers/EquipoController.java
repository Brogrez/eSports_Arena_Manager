package com.duoc.team_service.controllers;

import com.duoc.team_service.models.Equipo;
import com.duoc.team_service.repositories.EquipoRepository;
import com.duoc.team_service.services.EquipoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/equipos")

public class EquipoController {

    @Autowired
    private EquipoService equipoService;

    public EquipoController(EquipoService equipoService) {
        this.equipoService = equipoService;
    }


    @GetMapping
    public ResponseEntity<List<Equipo>> findAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(equipoService.findAll());
    }


    @GetMapping("/{id}")
    public ResponseEntity<Equipo> getById(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(equipoService.findByEquipoId(id));
    }


    @GetMapping("/capitan/{capitanId}")
    public ResponseEntity<Equipo> findByCapitanId(@PathVariable Long capitanId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(equipoService.findByCapitanId(capitanId));
    }


    @GetMapping("/estado/{estado}")
    public ResponseEntity<Equipo> findByEstado(@PathVariable String estado) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(equipoService.findByEstado(estado));
    }


    @GetMapping("/nombre/{nombreEquipo}")
    public ResponseEntity<Equipo> findByNombreEquipo(@PathVariable String nombreEquipo) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(equipoService.findByNombreEquipo(nombreEquipo));
    }


    @PostMapping
    public ResponseEntity<Equipo> save(@RequestBody Equipo equipo) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(equipoService.save(equipo));
    }


    @PutMapping("/{id}")
    public ResponseEntity<Equipo> update(@PathVariable Long id, @RequestBody Equipo equipo) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(equipoService.update(id, equipo));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        equipoService.deletebyId(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

}
