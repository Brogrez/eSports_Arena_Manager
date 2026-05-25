package com.duoc.controllers; // Alineado a tu ruta física real actual

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
                .body(this.prizeService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Prize> findById(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.prizeService.findById(id));

    }
    @GetMapping("/name/{name}")
    public ResponseEntity<Prize> findByName(@PathVariable String name) { // <-- Corregido a <Prize>
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.prizeService.findByName(name));
    }
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Prize>> findByEstado(@PathVariable String estado) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.prizeService.findByEstado(estado));
    }

    @GetMapping("/torneo/{torneoId}")
    public ResponseEntity<List<Prize>> findByTorneoId(@PathVariable Long torneoId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.prizeService.findByTorneoId(torneoId));
    }

    @PostMapping
    public ResponseEntity<Prize> save(@Valid @RequestBody Prize prize) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(this.prizeService.save(prize));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Prize> updateById(@PathVariable Long id, @Valid @RequestBody Prize prize) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.prizeService.updateById(id, prize));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        this.prizeService.deleteById(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}