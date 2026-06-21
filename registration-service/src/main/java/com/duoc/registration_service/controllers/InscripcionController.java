package com.duoc.registration_service.controllers;

import com.duoc.registration_service.models.Inscripcion;
import com.duoc.registration_service.models.dtos.InscripcionDTO;
import com.duoc.registration_service.services.InscripcionService;
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
@RequestMapping("/api/v1/inscripciones")
@Validated
public class InscripcionController {

    @Autowired
    private InscripcionService inscripcionService;



    @PostMapping
    @Operation(summary = "Guardado de inscripcion", description = "Esta es la forma de guardar una inscripcion")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Inscripcion a crear", required = true,
            content = @Content(schema = @Schema(implementation = Inscripcion.class))
    )
    @ApiResponse(responseCode = "201", description = "Inscripcion creada")
    public ResponseEntity<Inscripcion> save(@RequestBody @Valid Inscripcion inscripcion) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(inscripcionService.save(inscripcion));
    }

    @GetMapping
    @Operation(
            summary = "Listado de todas las inscripciones",
            description = "Se devuelve una colección HATEOAS con las inscripciones enriquecidas"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<List<InscripcionDTO>> findAll()  {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(inscripcionService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Busqueda de una inscripcion por id",
            description = "Se devuelve una inscripcion, en caso contrario se devuelve una excepcion"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inscripcion encontrada"),
            @ApiResponse(responseCode = "404", description = "Inscripcion no se encuentra en la BD")
    })
    public ResponseEntity<Inscripcion> findById(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(inscripcionService.findByInscripcionId(id));
    }

    @GetMapping("/torneo/{torneoId}")
    @Operation(
            summary = "Listado de inscripciones por torneo",
            description = "Se devuelven las inscripciones asociadas a un torneo como colección HATEOAS"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<List<Inscripcion>> findByTorneoId(@PathVariable Long torneoId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(List.of(inscripcionService.findByTorneoId(torneoId)));
    }

    @GetMapping("/jugador/{jugadorId}")
    @Operation(
            summary = "Listado de inscripciones por jugador",
            description = "Se devuelven las inscripciones asociadas a un jugador como colección HATEOAS"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<List<Inscripcion>> findByJugadorId(@PathVariable Long jugadorId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(List.of(inscripcionService.findByJugadorId(jugadorId)));
    }
    @GetMapping("/equipo/{equipoId}")
    @Operation(
            summary = "Listado de inscripciones por equipo",
            description = "Se devuelven las inscripciones asociadas a un equipo como colección HATEOAS"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<List<Inscripcion>> findByEquipoId(@PathVariable Long equipoId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(List.of(inscripcionService.findByEquipoId(equipoId)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizacion de inscripcion", description = "Se actualizan los datos de una inscripcion existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inscripcion actualizada"),
            @ApiResponse(responseCode = "404", description = "Inscripcion no se encuentra en la BD")
    })
    public ResponseEntity<Inscripcion> update(@PathVariable Long id, @RequestBody @Valid Inscripcion inscripcion) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(inscripcionService.updateById(id, inscripcion));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminacion de inscripcion", description = "Se elimina una inscripcion por su id")
    @ApiResponse(responseCode = "204", description = "Inscripcion eliminada")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        inscripcionService.deleteById(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
