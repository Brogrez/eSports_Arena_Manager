package com.duoc.registration_service.controllers;

import com.duoc.registration_service.models.Inscripcion;
import com.duoc.registration_service.models.dtos.InscripcionDTO;
import com.duoc.registration_service.services.InscripcionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inscripciones")
@Validated
public class InscripcionController {

    @Autowired
    private InscripcionService inscripcionService;



    @PostMapping
    public ResponseEntity<Inscripcion> save(@RequestBody @Valid Inscripcion inscripcion) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(inscripcionService.save(inscripcion));
    }

    @GetMapping
    public ResponseEntity<List<InscripcionDTO>> findAll()  {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(inscripcionService.findAll());
    }

    @GetMapping("/torneo/{torneoId}")
    public ResponseEntity<List<Inscripcion>> findByTorneoId(@PathVariable Long torneoId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(List.of(inscripcionService.findByTorneoId(torneoId)));
    }

    @GetMapping("/jugador/{jugadorId}")
    public ResponseEntity<List<Inscripcion>> findByJugadorId(@PathVariable Long jugadorId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(List.of(inscripcionService.findByJugadorId(jugadorId)));
    }
    @GetMapping("/equipo/{equipoId}")
    public ResponseEntity<List<Inscripcion>> findByEquipoId(@PathVariable Long equipoId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(List.of(inscripcionService.findByEquipoId(equipoId)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Inscripcion> update(@PathVariable Long id, @RequestBody @Valid Inscripcion inscripcion) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(inscripcionService.updateById(id, inscripcion));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        inscripcionService.deleteById(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

}
