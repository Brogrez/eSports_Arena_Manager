package com.duoc.tournament_service.controllers;

import com.duoc.tournament_service.models.Tour;
import com.duoc.tournament_service.services.TourService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/tournaments")
public class TourController {

    @Autowired
    private TourService tourService;

    @GetMapping
    @Operation(
            summary = "Listado de todos los torneos",
            description = "Se devuelve una colección HATEOAS con todos los torneos"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<List<Tour>> findAll() {
            return ResponseEntity.status(HttpStatus.OK).body(tourService.findAll());
    }

    // Buscar por ID
    @GetMapping("/{id}")
    @Operation(
            summary = "Busqueda de un torneo por id",
            description = "Se devuelve un torneo, en caso contrario se devuelve una excepcion"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Torneo encontrado"),
            @ApiResponse(responseCode = "404", description = "Torneo no se encuentra en la BD")
    })
    public ResponseEntity<Tour> findById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(tourService.findById(id));
    }

    // Crear torneo
    @PostMapping
    @Operation(summary = "Guardado de torneo", description = "Esta es la forma de guardar un torneo")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Torneo a crear", required = true,
            content = @Content(schema = @Schema(implementation = Tour.class))
    )
    @ApiResponse(responseCode = "201", description = "Torneo creado")
    public ResponseEntity<Tour> save(@Valid @RequestBody Tour tournament) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tourService.save(tournament));
    }

    // Actualizar torneo
    @PutMapping("/{id}")
    @Operation(summary = "Actualizacion de torneo", description = "Se actualizan los datos de un torneo existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Torneo actualizado"),
            @ApiResponse(responseCode = "404", description = "Torneo no se encuentra en la BD")
    })
    public ResponseEntity<Tour> updateById(@PathVariable Long id, @Valid @RequestBody Tour tournament) {
        return ResponseEntity.status(HttpStatus.OK).body(tourService.updateById(id, tournament));
    }

    // Eliminar torneo
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminacion de torneo", description = "Se elimina un torneo por su id")
    @ApiResponse(responseCode = "204", description = "Torneo eliminado")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        tourService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Listar por juego
    @GetMapping("/game/{gameId}")
    @Operation(
            summary = "Listado de torneos por juego",
            description = "Se devuelven los torneos asociados a un juego como colección HATEOAS"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<List<Tour>> findByGameId(@PathVariable Long gameId) {
        return ResponseEntity.status(HttpStatus.OK).body(tourService.findByGameId(gameId));
    }

    // Listar por estado
    @GetMapping("/estado/{estado}")
    @Operation(
            summary = "Busqueda de torneos por estado",
            description = "Se devuelven los torneos segun su estado como colección HATEOAS"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<List<Tour>> findByEstado(@PathVariable String estado) {
        return ResponseEntity.status(HttpStatus.OK).body(tourService.findByEstado(estado));
    }

    // Listar por fecha de inicio
    @GetMapping("/fecha/{fechaInicio}")
    @Operation(
            summary = "Busqueda de torneos por fecha de inicio",
            description = "Se devuelven los torneos segun su fecha de inicio como colección HATEOAS"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<List<Tour>> findByFechaInicio(@PathVariable LocalDate fechaInicio) {
        return ResponseEntity.status(HttpStatus.OK).body(tourService.findByFechaInicio(fechaInicio));
    }

    // Cancelar torneo
    @PatchMapping("/{id}/cancelar")
    @Operation(summary = "Cancelacion de torneo", description = "Se cancela un torneo existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Torneo cancelado"),
            @ApiResponse(responseCode = "404", description = "Torneo no se encuentra en la BD")
    })
    public ResponseEntity<Tour> cancelar(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(tourService.cancelar(id));
    }

    // Cerrar torneo
    @PatchMapping("/{id}/cerrar")
    @Operation(summary = "Cierre de torneo", description = "Se cierra un torneo existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Torneo cerrado"),
            @ApiResponse(responseCode = "404", description = "Torneo no se encuentra en la BD")
    })
    public ResponseEntity<Tour> cerrar(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(tourService.cerrar(id));
    }
}
