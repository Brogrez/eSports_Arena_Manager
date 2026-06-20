package com.duoc.registration_service.controllers;

import com.duoc.registration_service.models.Inscripcion;
import com.duoc.registration_service.models.dtos.InscripcionDTO;
import com.duoc.registration_service.services.InscripcionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/inscripciones")
@Validated
@Tag(name = "Inscripciones V1", description = "Metodos CRUD para la gestión de inscripciones")
public class InscripcionController {

    @Autowired
    private InscripcionService inscripcionService;


    @GetMapping
    @Operation(
            summary = "Listado de todas las inscripciones",
            description = "Se devuelve una lista con las inscripciones enriquecidas en formato DTO"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<List<InscripcionDTO>> findAll() {
        return ResponseEntity.ok(inscripcionService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Busqueda de una inscripcion por id",
            description = "Se devuelve una inscripcion, en caso contrario se devuelve una excepcion"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Inscripcion encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Inscripcion.class))),
            @ApiResponse(responseCode = "404", description = "Inscripcion no se encuentra en la BD")
    })
    public ResponseEntity<Inscripcion> findById(
            @Parameter(description = "Id de la inscripcion a buscar", required = true, example = "1")
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(inscripcionService.findByInscripcionId(id));
    }

    // POST /api/v1/inscripciones => crear. @Valid valida el body; @RequestBody convierte el JSON.
    @PostMapping
    @Operation(summary = "Guardado de inscripcion", description = "Esta es la forma de guardar una inscripcion")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Inscripcion a crear", required = true,
            content = @Content(schema = @Schema(implementation = Inscripcion.class))
    )
    @ApiResponse(responseCode = "201", description = "Inscripcion creada")
    public ResponseEntity<Inscripcion> save(@Valid @RequestBody Inscripcion inscripcion) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(inscripcionService.save(inscripcion));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizacion de inscripcion", description = "Se actualizan los datos de una inscripcion existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inscripcion actualizada"),
            @ApiResponse(responseCode = "404", description = "Inscripcion no se encuentra en la BD")
    })
    public ResponseEntity<Inscripcion> update(
            @Parameter(description = "Id de la inscripcion a actualizar", required = true, example = "1")
            @PathVariable Long id,
            @Valid @RequestBody Inscripcion inscripcion
    ) {
        return ResponseEntity.ok(inscripcionService.updateById(id, inscripcion));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminacion de inscripcion", description = "Se elimina una inscripcion por su id")
    @ApiResponse(responseCode = "204", description = "Inscripcion eliminada")
    public ResponseEntity<Void> deleteById(
            @Parameter(description = "Id de la inscripcion a eliminar", required = true, example = "1")
            @PathVariable Long id
    ) {
        inscripcionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/torneo/{torneoId}")
    @Operation(
            summary = "Listado de inscripciones por torneo",
            description = "Se devuelven las inscripciones asociadas a un torneo especifico"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<List<Inscripcion>> findByTorneoId(
            @Parameter(description = "Id del torneo", required = true, example = "1")
            @PathVariable Long torneoId
    ) {
        return ResponseEntity.ok(List.of(inscripcionService.findByTorneoId(torneoId)));
    }

    @GetMapping("/jugador/{jugadorId}")
    @Operation(
            summary = "Listado de inscripciones por jugador",
            description = "Se devuelven las inscripciones asociadas a un jugador especifico"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<List<Inscripcion>> findByJugadorId(
            @Parameter(description = "Id del jugador", required = true, example = "1")
            @PathVariable Long jugadorId
    ) {
        return ResponseEntity.ok(List.of(inscripcionService.findByJugadorId(jugadorId)));
    }

    @GetMapping("/equipo/{equipoId}")
    @Operation(
            summary = "Listado de inscripciones por equipo",
            description = "Se devuelven las inscripciones asociadas a un equipo especifico"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<List<Inscripcion>> findByEquipoId(
            @Parameter(description = "Id del equipo", required = true, example = "1")
            @PathVariable Long equipoId
    ) {
        return ResponseEntity.ok(List.of(inscripcionService.findByEquipoId(equipoId)));
    }
}