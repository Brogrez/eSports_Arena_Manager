package eSports_Arena_Manager.sanction_service.controllers;


import eSports_Arena_Manager.sanction_service.models.Sanction;
import eSports_Arena_Manager.sanction_service.services.SanctionService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sanctions")
@Validated
public class SanctionController {
    @Autowired
    private SanctionService sanctionService;

    @GetMapping
    public ResponseEntity<List<Sanction>> findAll(){
        return ResponseEntity.status(HttpStatus.OK).body(sanctionService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sanction> findById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(sanctionService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Sanction> save(@Valid @RequestBody Sanction sanction){
        return ResponseEntity.status(HttpStatus.CREATED).body(sanctionService.save(sanction));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sanction> updateById(@PathVariable Long id, @Valid @RequestBody Sanction sanction){
        return ResponseEntity.status(HttpStatus.OK).body(sanctionService.updateById(id, sanction));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteByid(@PathVariable Long id){
        sanctionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Sanction>> findByUsuarioId(@PathVariable Long userId){
        return ResponseEntity.status(HttpStatus.OK).body(sanctionService.findByUsuarioId(userId));
    }

    @GetMapping("/team/{teamId}")
    public ResponseEntity<List<Sanction>> findByTeamId(@PathVariable Long teamId){
        return ResponseEntity.status(HttpStatus.OK).body(sanctionService.findByTeamId(teamId));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Sanction>> findByEstado(@PathVariable String estado){
        return ResponseEntity.status(HttpStatus.OK).body(sanctionService.findByEstado(estado));
    }

    @PatchMapping("/{id}/cerrar")
    public ResponseEntity<Sanction> cerrar(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(sanctionService.cerrar(id));
    }
}
