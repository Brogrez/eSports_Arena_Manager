package com.example.match_service.controllers;

import com.example.match_service.models.Match;
import com.example.match_service.services.MatchService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/matchs")
@Validated
public class MatchController {
    @Autowired
    private MatchService matchService;

    @GetMapping
    public ResponseEntity<List<Match>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(matchService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Match> findById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(matchService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Match> save(@Valid @RequestBody Match match) {
        return ResponseEntity.status(HttpStatus.CREATED).body(matchService.save(match));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Match> updateById(@PathVariable Long id, @Valid @RequestBody Match match) {
        return ResponseEntity.status(HttpStatus.OK).body(matchService.updateById(match, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        matchService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/tour/{tourId}")
    public ResponseEntity<List<Match>> findByTorneoId(@PathVariable Long tourId) {
        return ResponseEntity.status(HttpStatus.OK).body(matchService.findByTorneoId(tourId));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Match>> findByEstado(@PathVariable String estado) {
        return ResponseEntity.status(HttpStatus.OK).body(matchService.findByEstado(estado));
    }

    @GetMapping("/round/{round}")
    public ResponseEntity<List<Match>> findByRonda(@PathVariable String round) {
        return ResponseEntity.status(HttpStatus.OK).body(matchService.findByRonda(round));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Match> cancelar(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(matchService.cancelar(id));
    }
}
