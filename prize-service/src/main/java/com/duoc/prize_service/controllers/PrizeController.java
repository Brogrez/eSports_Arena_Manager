package com.duoc.prize_service.controllers;

import com.duoc.prize_service.models.Prize;
import com.duoc.prize_service.services.PrizeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/prizes")
public class PrizeController {

    @Autowired
    private PrizeService prizeService;

    @GetMapping
    public ResponseEntity<List<Prize>> getAllPrizes() {
        return ResponseEntity.ok(this.prizeService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Prize> getPrizeById(@PathVariable Long id) {
        return ResponseEntity.ok(this.prizeService.findById(id));
    }

    @GetMapping("/tournament/{torneoId}")
    public ResponseEntity<List<Prize>> getPrizesByTorneoId(@PathVariable Long torneoId) {
        return ResponseEntity.ok(this.prizeService.findByTorneoId(torneoId));
    }

    @PostMapping
    public ResponseEntity<Prize> createPrize(@Valid @RequestBody Prize prize) {
        Prize nuevoPremio = this.prizeService.save(prize);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPremio);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Prize> updatePrize(@PathVariable Long id, @Valid @RequestBody Prize prize) {
        return ResponseEntity.ok(this.prizeService.updateById(id, prize));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrize(@PathVariable Long id) {
        this.prizeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}