package com.example.ranking_service.controllers;

import com.example.ranking_service.models.Ranking;
import com.example.ranking_service.services.RankingService;
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
@RequestMapping("/api/v1/rankings")
@Validated
public class RankingController {

    @Autowired
    private RankingService rankingService;

    @GetMapping
    @Operation(
            summary = "Listado de todos los rankings",
            description = "Se devuelve una colección HATEOAS con todos los rankings"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<List<Ranking>> findAll(){
        return ResponseEntity.status(HttpStatus.OK).body(rankingService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Busqueda de un ranking por id",
            description = "Se devuelve un ranking, en caso contrario se devuelve una excepcion"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ranking encontrado"),
            @ApiResponse(responseCode = "404", description = "Ranking no se encuentra en la BD")
    })
    public ResponseEntity<Ranking> findById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(rankingService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Guardado de ranking", description = "Esta es la forma de guardar un registro de ranking")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Ranking a crear", required = true,
            content = @Content(schema = @Schema(implementation = Ranking.class))
    )
    @ApiResponse(responseCode = "201", description = "Ranking creado")
    public ResponseEntity<Ranking> save(@Valid @RequestBody Ranking ranking) {
        return ResponseEntity.status(HttpStatus.CREATED).body(rankingService.save(ranking));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizacion de ranking", description = "Se actualizan los datos de un ranking existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ranking actualizado"),
            @ApiResponse(responseCode = "404", description = "Ranking no se encuentra en la BD")
    })
    public ResponseEntity<Ranking> updateById(@PathVariable Long id, @Valid @RequestBody Ranking ranking) {
        return ResponseEntity.status(HttpStatus.OK).body(rankingService.updateById(ranking, id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminacion de ranking", description = "Se elimina un ranking por su id")
    @ApiResponse(responseCode = "204", description = "Ranking eliminado")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        rankingService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/tournament/{tourId}")
    @Operation(
            summary = "Listado de rankings por torneo",
            description = "Se devuelven los rankings asociados a un torneo como colección HATEOAS"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<List<Ranking>> findByTourId(@PathVariable Long tourId) {
        return ResponseEntity.status(HttpStatus.OK).body(rankingService.findByTourId(tourId));
    }

    @GetMapping("/tournament/{tourId}/participante/{participanteId}")
    @Operation(
            summary = "Busqueda de ranking por torneo y participante",
            description = "Se devuelve la posicion de un participante en un torneo como recurso HATEOAS"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ranking encontrado"),
            @ApiResponse(responseCode = "404", description = "Ranking no se encuentra en la BD")
    })
    public ResponseEntity<Ranking> findByTourIdAndParticipanteId(@PathVariable Long tourId, @PathVariable Long participanteId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(rankingService.findByTourIdAndParticipanteId(tourId, participanteId));
    }

    @PatchMapping("/{id}/puntos")
    @Operation(summary = "Actualizacion de puntos", description = "Actualiza puntos, victorias y derrotas de un ranking")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Puntos actualizados"),
            @ApiResponse(responseCode = "404", description = "Ranking no se encuentra en la BD")
    })
    public ResponseEntity<Ranking> actualizarPuntos(
            @PathVariable Long id,
            @RequestParam Integer puntos,
            @RequestParam Integer victorias,
            @RequestParam Integer derrotas) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(rankingService.actualizarPuntos(id, puntos, victorias, derrotas));
    }

    @PatchMapping("/tournament/{tourId}/cerrar")
    @Operation(summary = "Cierre de ranking", description = "Cierra el ranking de un torneo al finalizar")
    @ApiResponse(responseCode = "204", description = "Ranking cerrado")
    public ResponseEntity<Void> cerrarRanking(@PathVariable Long tourId) {
        rankingService.cerrarRanking(tourId);
        return ResponseEntity.noContent().build();
    }
}
