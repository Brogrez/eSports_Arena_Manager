package eSports_Arena_Manager.sanction_service.controllers;

import eSports_Arena_Manager.sanction_service.models.Sanction;
import eSports_Arena_Manager.sanction_service.services.SanctionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sanctions")
public class SanctionController {

    @Autowired
    private SanctionService sanctionService;

    @GetMapping
    public List<Sanction> getAll() {
        return sanctionService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(sanctionService.findById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(sanctionService.findByUsuarioId(userId));
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Sanction sanction, BindingResult result) {
        if (result.hasErrors()) {
            return validate(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(sanctionService.save(sanction));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody Sanction sanction, BindingResult result, @PathVariable Long id) {
        if (result.hasErrors()) {
            return validate(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(sanctionService.updateById(id, sanction));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        sanctionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Método para manejar los mensajes de error de validación (estándar de Spring)
    private ResponseEntity<Map<String, String>> validate(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }
}
