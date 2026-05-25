package com.example.ranking_service.controllers;

import com.example.ranking_service.models.Ranking;
import com.example.ranking_service.services.RankingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rankings")
@Validated
public class RankingController {

    @Autowired
    private RankingService rankingService;

    @GetMapping
    public ResponseEntity<List<Ranking>> findAll(){
        return ResponseEntity.status(HttpStatus.OK).body(rankingService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ranking> findById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(rankingService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Ranking> save(@Valid @RequestBody Ranking ranking) {
        return ResponseEntity.status(HttpStatus.CREATED).body(rankingService.save(ranking));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ranking> updateById(@PathVariable Long id, @Valid @RequestBody Ranking ranking) {
        return ResponseEntity.status(HttpStatus.OK).body(rankingService.updateById(ranking, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        rankingService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/tournament/{tourId}")
    public ResponseEntity<List<Ranking>> findByTourId(@PathVariable Long tourId) {
        return ResponseEntity.status(HttpStatus.OK).body(rankingService.findByTourId(tourId));
    }

    @GetMapping("/tournament/{tourId}/participante/{participanteId}")
    public ResponseEntity<Ranking> findByTourIdAndParticipanteId(@PathVariable Long tourId, @PathVariable Long participanteId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(rankingService.findByTourIdAndParticipanteId(tourId, participanteId));
    }

    @PatchMapping("/{id}/puntos")
    public ResponseEntity<Ranking> actualizarPuntos(
            @PathVariable Long id,
            @RequestParam Integer puntos,
            @RequestParam Integer victorias,
            @RequestParam Integer derrotas) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(rankingService.actualizarPuntos(id, puntos, victorias, derrotas));
    }

    @PatchMapping("/tournament/{tourId}/cerrar")
    public ResponseEntity<Void> cerrarRanking(@PathVariable Long tourId) {
        rankingService.cerrarRanking(tourId);
        return ResponseEntity.noContent().build();
    }
}
