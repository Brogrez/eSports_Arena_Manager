package com.duoc.team_service.controllers;

import com.duoc.team_service.models.Equipo;
import com.duoc.team_service.models.dtos.EquipoDTO;
import com.duoc.team_service.repositories.EquipoRepository;
import com.duoc.team_service.services.EquipoService;
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
@RequestMapping("/api/v1/equipos")
@Validated
@Tag(name = "Equipos V1", description = "Metodos CRUD para la gestión de equipos")
public class EquipoController {

    @Autowired
    private EquipoService equipoService;


    @GetMapping
    @Operation(
            summary = "Listado de todos los equipos",
            description = "Se devuelve una lista con los equipos enriquecidos en formato DTO"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<List<EquipoDTO>> findAll() {
        return ResponseEntity.ok(equipoService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Busqueda de un equipo por id",
            description = "Se devuelve un equipo, en caso contrario se devuelve una excepcion"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Equipo encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Equipo.class))),
            @ApiResponse(responseCode = "404", description = "Equipo no se encuentra en la BD")
    })
    public ResponseEntity<Equipo> getById(
            @Parameter(description = "Id del equipo a buscar", required = true, example = "1")
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(equipoService.findByEquipoId(id));
    }

    @GetMapping("/capitan/{capitanId}")
    @Operation(
            summary = "Busqueda de equipo por capitan",
            description = "Se devuelve el equipo asociado a un capitan"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<Equipo> findByCapitanId(
            @Parameter(description = "Id del capitan", required = true, example = "1")
            @PathVariable Long capitanId
    ) {
        return ResponseEntity.ok(equipoService.findByCapitanId(capitanId));
    }

    @GetMapping("/estado/{estado}")
    @Operation(
            summary = "Busqueda de equipo por estado",
            description = "Se devuelve el equipo asociado a un estado especifico"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<Equipo> findByEstado(
            @Parameter(description = "Estado del equipo a buscar", required = true, example = "Activo")
            @PathVariable String estado
    ) {
        return ResponseEntity.ok(equipoService.findByEstado(estado));
    }

    @GetMapping("/nombre/{nombreEquipo}")
    @Operation(
            summary = "Busqueda de equipo por nombre",
            description = "Se devuelve el equipo buscando por su nombre exacto"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<Equipo> findByNombreEquipo(
            @Parameter(description = "Nombre del equipo a buscar", required = true, example = "Team Alpha")
            @PathVariable String nombreEquipo
    ) {
        return ResponseEntity.ok(equipoService.findByNombreEquipo(nombreEquipo));
    }


    @PostMapping
    @Operation(summary = "Guardado de equipo", description = "Esta es la forma de guardar un equipo")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Equipo a crear", required = true,
            content = @Content(schema = @Schema(implementation = Equipo.class))
    )
    @ApiResponse(responseCode = "201", description = "Equipo creado")
    public ResponseEntity<Equipo> save(@Valid @RequestBody Equipo equipo) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(equipoService.save(equipo));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizacion de equipo", description = "Se actualizan los datos de un equipo existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Equipo actualizado"),
            @ApiResponse(responseCode = "404", description = "Equipo no se encuentra en la BD")
    })
    public ResponseEntity<Equipo> update(
            @Parameter(description = "Id del equipo a actualizar", required = true, example = "1")
            @PathVariable Long id,
            @Valid @RequestBody Equipo equipo
    ) {
        return ResponseEntity.ok(equipoService.update(id, equipo));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminacion de equipo", description = "Se elimina un equipo por su id")
    @ApiResponse(responseCode = "204", description = "Equipo eliminado")
    public ResponseEntity<Void> deleteById(
            @Parameter(description = "Id del equipo a eliminar", required = true, example = "1")
            @PathVariable Long id
    ) {
        equipoService.deletebyId(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/games/{juegoId}")
    @Operation(
            summary = "Listado de equipos por juego principal",
            description = "Se devuelven los equipos asociados a un juego principal"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<List<Equipo>> findByJuegoPrincipalId(
            @Parameter(description = "Id del juego principal", required = true, example = "1")
            @PathVariable Long juegoId
    ) {
        return ResponseEntity.ok(equipoService.findByJuegoPrincipalId(juegoId));
    }
}
