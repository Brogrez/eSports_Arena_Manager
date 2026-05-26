package com.duoc.prize_service.controllers;

import com.duoc.prize_service.models.PremioAsignado;
import com.duoc.prize_service.models.Prize;
import com.duoc.prize_service.services.PrizeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/prizes")
@Validated
public class PrizeController {

    @Autowired
    private PrizeService prizeService;

    @GetMapping
    public ResponseEntity<List<Prize>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(prizeService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Prize> findById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(prizeService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Prize> save(@Valid @RequestBody Prize premio) {
        return ResponseEntity.status(HttpStatus.CREATED).body(prizeService.save(premio));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Prize> updateById(@PathVariable Long id, @Valid @RequestBody Prize prize) {
        return ResponseEntity.status(HttpStatus.OK).body(prizeService.updateById(id, prize));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        prizeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/torneo/{torneoId}")
    public ResponseEntity<List<Prize>> findByTorneoId(@PathVariable Long torneoId) {
        return ResponseEntity.status(HttpStatus.OK).body(prizeService.findByTorneoId(torneoId));
    }

    @GetMapping("/posicion/{posicion}")
    public ResponseEntity<List<Prize>> findByPosicion(@PathVariable Integer posicion) {
        return ResponseEntity.status(HttpStatus.OK).body(prizeService.findByPosicion(posicion));
    }

    @PostMapping("/{premioId}/asignar/{participanteId}")
    public ResponseEntity<PremioAsignado> asignarPremio(
            @PathVariable Long premioId,
            @PathVariable Long participanteId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(prizeService.asignarPremio(premioId, participanteId));
    }
}