package com.duoc.tournament_service.controllers;

import com.duoc.tournament_service.models.Tour;
import com.duoc.tournament_service.services.TourService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/tournaments")
public class TourController {

    @Autowired
    private TourService tourService;

    @GetMapping
    public ResponseEntity<List<Tour>> findAll() {
            return ResponseEntity.status(HttpStatus.OK).body(tourService.findAll());
    }

    // Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<Tour> findById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(tourService.findById(id));
    }

    // Crear torneo
    @PostMapping
    public ResponseEntity<Tour> save(@Valid @RequestBody Tour tournament) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tourService.save(tournament));
    }

    // Actualizar torneo
    @PutMapping("/{id}")
    public ResponseEntity<Tour> updateById(@PathVariable Long id, @Valid @RequestBody Tour tournament) {
        return ResponseEntity.status(HttpStatus.OK).body(tourService.updateById(id, tournament));
    }

    // Eliminar torneo
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        tourService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Listar por juego
    @GetMapping("/game/{gameId}")
    public ResponseEntity<List<Tour>> findByGameId(@PathVariable Long gameId) {
        return ResponseEntity.status(HttpStatus.OK).body(tourService.findByGameId(gameId));
    }

    // Listar por estado
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Tour>> findByEstado(@PathVariable String estado) {
        return ResponseEntity.status(HttpStatus.OK).body(tourService.findByEstado(estado));
    }

    // Listar por fecha de inicio
    @GetMapping("/fecha/{fechaInicio}")
    public ResponseEntity<List<Tour>> findByFechaInicio(@PathVariable LocalDate fechaInicio) {
        return ResponseEntity.status(HttpStatus.OK).body(tourService.findByFechaInicio(fechaInicio));
    }

    // Cancelar torneo
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Tour> cancelar(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(tourService.cancelar(id));
    }

    // Cerrar torneo
    @PatchMapping("/{id}/cerrar")
    public ResponseEntity<Tour> cerrar(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(tourService.cerrar(id));
    }
}
