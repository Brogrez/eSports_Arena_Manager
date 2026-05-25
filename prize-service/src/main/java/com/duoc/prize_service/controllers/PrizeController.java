package com.duoc.prize_service.controllers;

import com.duoc.prize_service.models.Prize;
import com.duoc.prize_service.models.dtos.PrizeSaveDTO;
import com.duoc.prize_service.services.PrizeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/prizes")
@RequiredArgsConstructor
public class PrizeController {

    @Autowired
    private PrizeService prizeService;

    @GetMapping
    public ResponseEntity<List<PrizeSaveDTO>> findAll() {
        return ResponseEntity.
                status(HttpStatus.OK).body(prizeService.findAll());
    }


    @GetMapping("/{id}")
    public ResponseEntity<Prize> getPrizeById(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(prizeService.findById(id));
    }

    @GetMapping("/tournament/{torneoId}")
    public ResponseEntity<List<Prize>> getPrizesByTorneoId(@PathVariable Long torneoId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(prizeService.findByTorneoId(torneoId));
    }

    @PostMapping
    public ResponseEntity<Prize> save(@Valid @RequestBody Prize prize) {
        return  ResponseEntity
                .status(HttpStatus.CREATED)
                .body(prizeService.save(prize));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrize(@PathVariable Long id) {
        prizeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}