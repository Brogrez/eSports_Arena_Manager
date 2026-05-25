package eSports_Arena_Manager.result_service.controllers;

import eSports_Arena_Manager.result_service.models.Result;
import eSports_Arena_Manager.result_service.services.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/results")
public class ResultController {

    @Autowired
    private ResultService resultService;

    @GetMapping
    public ResponseEntity<List<Result>> getAllResults() {
        return ResponseEntity.ok(this.resultService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Result> getResultById(@PathVariable Long id) {
        return ResponseEntity.ok(this.resultService.findById(id));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Result>> getResultsByEstado(@PathVariable String estado) {
        return ResponseEntity.ok(this.resultService.findByEstado(estado));
    }

    @PostMapping
    public ResponseEntity<Result> createResult(@RequestBody Result result) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.resultService.save(result));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Result> updateResult(@PathVariable Long id, @RequestBody Result result) {
        return ResponseEntity.ok(this.resultService.updateById(id, result));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResult(@PathVariable Long id) {
        this.resultService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}