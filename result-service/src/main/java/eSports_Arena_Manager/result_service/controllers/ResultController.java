package eSports_Arena_Manager.result_service.controllers;

import eSports_Arena_Manager.result_service.models.Result;
import eSports_Arena_Manager.result_service.services.ResultService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/results")
@Validated
public class ResultController {

    @Autowired
    private ResultService resultService;

    @GetMapping
    public ResponseEntity<List<Result>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(resultService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Result> findById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(resultService.findById(id));
    }

    @GetMapping("/partida/{partidaId}")
    public ResponseEntity<Result> findByPartidaId(@PathVariable Long partidaId) {
        return ResponseEntity.status(HttpStatus.OK).body(resultService.findByPartidaId(partidaId));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Result>> findByEstado(@PathVariable String estado) {
        return ResponseEntity.status(HttpStatus.OK).body(resultService.findByEstado(estado));
    }

    @PostMapping
    public ResponseEntity<Result> save(@Valid @RequestBody Result result) {
        return ResponseEntity.status(HttpStatus.CREATED).body(resultService.save(result));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Result> updateById(@PathVariable Long id, @Valid @RequestBody Result result) {
        return ResponseEntity.status(HttpStatus.OK).body(resultService.updateById(id, result));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        resultService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}