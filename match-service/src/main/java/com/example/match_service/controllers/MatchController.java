package com.example.match_service.controllers;

import com.example.match_service.models.Match;
import com.example.match_service.services.MatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/matchs")
@Validated
public class MatchController {
    @Autowired
    private MatchService matchService;

    @GetMapping
    @Operation(
            summary = "Listado de todas las partidas",
            description = "Se devuelve una colección HATEOAS con todas las partidas"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<List<Match>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(matchService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Busqueda de una partida por id",
            description = "Se devuelve una partida, en caso contrario se devuelve una excepcion"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Partida encontrada"),
            @ApiResponse(responseCode = "404", description = "Partida no se encuentra en la BD")
    })
    public ResponseEntity<Match> findById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(matchService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Guardado de partida", description = "Esta es la forma de guardar una partida")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Partida a crear", required = true,
            content = @Content(schema = @Schema(implementation = Match.class))
    )
    @ApiResponse(responseCode = "201", description = "Partida creada")
    public ResponseEntity<Match> save(@Valid @RequestBody Match match) {
        return ResponseEntity.status(HttpStatus.CREATED).body(matchService.save(match));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizacion de partida", description = "Se actualizan los datos de una partida existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Partida actualizada"),
            @ApiResponse(responseCode = "404", description = "Partida no se encuentra en la BD")
    })
    public ResponseEntity<Match> updateById(@PathVariable Long id, @Valid @RequestBody Match match) {
        return ResponseEntity.status(HttpStatus.OK).body(matchService.updateById(match, id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminacion de partida", description = "Se elimina una partida por su id")
    @ApiResponse(responseCode = "204", description = "Partida eliminada")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        matchService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/tour/{tourId}")
    @Operation(
            summary = "Listado de partidas por torneo",
            description = "Se devuelven las partidas asociadas a un torneo como colección HATEOAS"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<List<Match>> findByTorneoId(@PathVariable Long tourId) {
        return ResponseEntity.status(HttpStatus.OK).body(matchService.findByTorneoId(tourId));
    }

    @GetMapping("/estado/{estado}")
    @Operation(
            summary = "Busqueda de partidas por estado",
            description = "Se devuelven las partidas segun su estado como colección HATEOAS"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<List<Match>> findByEstado(@PathVariable String estado) {
        return ResponseEntity.status(HttpStatus.OK).body(matchService.findByEstado(estado));
    }

    @GetMapping("/round/{round}")
    @Operation(
            summary = "Busqueda de partidas por ronda",
            description = "Se devuelven las partidas segun su ronda como colección HATEOAS"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<List<Match>> findByRonda(@PathVariable String round) {
        return ResponseEntity.status(HttpStatus.OK).body(matchService.findByRonda(round));
    }

    @PatchMapping("/{id}/cancelar")
    @Operation(summary = "Cancelacion de partida", description = "Se cancela una partida existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Partida cancelada"),
            @ApiResponse(responseCode = "404", description = "Partida no se encuentra en la BD")
    })
    public ResponseEntity<Match> cancelar(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(matchService.cancelar(id));
    }
}
