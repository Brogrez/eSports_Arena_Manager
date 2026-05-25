package com.duoc.prize_service.controllers;

import com.duoc.prize_service.models.Prize;
import com.duoc.prize_service.models.dtos.PrizeSaveDTO;
import com.duoc.prize_service.services.PrizeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/prizes")
@RequiredArgsConstructor
public class PrizeController {

    private final PrizeService prizeService;

    @GetMapping
    public ResponseEntity<List<Prize>> getAllPrizes() {
        return ResponseEntity.ok(prizeService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Prize> getPrizeById(@PathVariable Long id) {
        return ResponseEntity.ok(prizeService.findById(id));
    }

    @GetMapping("/tournament/{torneoId}")
    public ResponseEntity<List<Prize>> getPrizesByTorneoId(@PathVariable Long torneoId) {
        return ResponseEntity.ok(prizeService.findByTorneoId(torneoId));
    }

    @PostMapping
    public ResponseEntity<Prize> createPrize(@Valid @RequestBody PrizeSaveDTO prizeSaveDTO) {
        Prize nuevoPremio = prizeService.save(prizeSaveDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPremio);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrize(@PathVariable Long id) {
        prizeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}