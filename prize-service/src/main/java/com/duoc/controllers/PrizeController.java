package com.duoc.controllers;

import com.duoc.models.Prize;
import com.duoc.services.PrizeService;
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
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(prizeService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Prize> findById(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(prizeService.findById(id));
    }

    @GetMapping("/torneo/{torneoId}")
    public ResponseEntity<List<Prize>> findByTorneoId(@PathVariable Long torneoId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(prizeService.findByTorneoId(torneoId));
    }

    @PostMapping
    public ResponseEntity<Prize> save(@Valid @RequestBody Prize prize) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(prizeService.save(prize));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Prize> update(@PathVariable Long id, @Valid @RequestBody Prize prize) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(prizeService.updateById(id, prize));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        prizeService.deleteById(id);
        return ResponseEntity
                .noContent()
                .build();
    }
}