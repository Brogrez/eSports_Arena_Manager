package com.duoc.team_service.controllers;

import com.duoc.team_service.assemblers.EquipoModelAssembler;
import com.duoc.team_service.models.Equipo;
import com.duoc.team_service.models.dtos.EquipoDTO;
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
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v2/equipos")
@Validated
@Tag(name = "Equipos V2", description = "Metodos CRUD HATEOAS para la gestión de equipos")
public class EquipoControllerV2 {

    @Autowired
    private EquipoService equipoService;

    @Autowired
    private EquipoModelAssembler equipoModelAssembler;

    @GetMapping
    @Operation(
            summary = "Listado de todos los equipos",
            description = "Se devuelve una colección HATEOAS con los equipos"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<CollectionModel<EquipoDTO>> findAll() {
        List<EquipoDTO> equipos = this.equipoService.findAll();
        CollectionModel<EquipoDTO> collectionModel = CollectionModel.of(
                equipos,
                linkTo(methodOn(EquipoControllerV2.class).findAll()).withSelfRel()
        );
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(collectionModel);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Busqueda de un equipo por id",
            description = "Se devuelve un equipo, en caso contrario se devuelve una excepcion"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Equipo encontrado"),
            @ApiResponse(responseCode = "404", description = "Equipo no se encuentra en la BD")
    })
    public ResponseEntity<EntityModel<Equipo>> findById(
            @Parameter(description = "Id del equipo a buscar", required = true, example = "1")
            @PathVariable Long id
    ) {
        EntityModel<Equipo> entityModel = this.equipoModelAssembler.toModel(
                this.equipoService.findByEquipoId(id)
        );
        return ResponseEntity.ok(entityModel);
    }

    @PostMapping
    @Operation(summary = "Guardado de equipo", description = "Esta es la forma de guardar un equipo")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Equipo a crear", required = true,
            content = @Content(schema = @Schema(implementation = Equipo.class))
    )
    @ApiResponse(responseCode = "201", description = "Equipo creado")
    public ResponseEntity<EntityModel<Equipo>> save(@Valid @RequestBody Equipo equipo) {
        Equipo equipoCreate = this.equipoService.save(equipo);
        EntityModel<Equipo> entityModel = this.equipoModelAssembler.toModel(equipoCreate);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(entityModel);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizacion de equipo", description = "Se actualizan los datos de un equipo existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Equipo actualizado"),
            @ApiResponse(responseCode = "404", description = "Equipo no se encuentra en la BD")
    })
    public ResponseEntity<EntityModel<Equipo>> update(
            @Parameter(description = "Id del equipo a actualizar", required = true, example = "1")
            @PathVariable Long id,
            @Valid @RequestBody Equipo equipo
    ) {
        Equipo equipoUpdate = this.equipoService.update(id, equipo);
        EntityModel<Equipo> entityModel = this.equipoModelAssembler.toModel(equipoUpdate);
        return ResponseEntity.ok(entityModel);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminacion de equipo", description = "Se elimina un equipo por su id")
    @ApiResponse(responseCode = "204", description = "Equipo eliminado")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Id del equipo a eliminar", required = true, example = "1")
            @PathVariable Long id
    ) {
        this.equipoService.deletebyId(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/capitan/{capitanId}")
    @Operation(
            summary = "Busqueda de equipo por capitan",
            description = "Se devuelve el equipo asociado a un capitan como recurso HATEOAS"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<EntityModel<Equipo>> findByCapitanId(
            @Parameter(description = "Id del capitan", required = true, example = "1")
            @PathVariable Long capitanId
    ) {
        EntityModel<Equipo> entityModel = this.equipoModelAssembler.toModel(
                this.equipoService.findByCapitanId(capitanId)
        );
        return ResponseEntity.ok(entityModel);
    }

    @GetMapping("/estado/{estado}")
    @Operation(
            summary = "Busqueda de equipo por estado",
            description = "Se devuelve el equipo asociado a un estado como recurso HATEOAS"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<EntityModel<Equipo>> findByEstado(
            @Parameter(description = "Estado del equipo", required = true, example = "ACTIVO")
            @PathVariable String estado
    ) {
        EntityModel<Equipo> entityModel = this.equipoModelAssembler.toModel(
                this.equipoService.findByEstado(estado)
        );
        return ResponseEntity.ok(entityModel);
    }

    @GetMapping("/nombre/{nombreEquipo}")
    @Operation(
            summary = "Busqueda de equipo por nombre",
            description = "Se devuelve el equipo asociado a un nombre como recurso HATEOAS"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<EntityModel<Equipo>> findByNombreEquipo(
            @Parameter(description = "Nombre del equipo", required = true, example = "Team Alpha")
            @PathVariable String nombreEquipo
    ) {
        EntityModel<Equipo> entityModel = this.equipoModelAssembler.toModel(
                this.equipoService.findByNombreEquipo(nombreEquipo)
        );
        return ResponseEntity.ok(entityModel);
    }

    @GetMapping("/games/{juegoId}")
    @Operation(
            summary = "Listado de equipos por juego principal",
            description = "Se devuelven los equipos asociados a un juego como colección HATEOAS"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<CollectionModel<EntityModel<Equipo>>> findByJuegoPrincipalId(
            @Parameter(description = "Id del juego", required = true, example = "1")
            @PathVariable Long juegoId
    ) {
        List<EntityModel<Equipo>> entityModels = this.equipoService.findByJuegoPrincipalId(juegoId)
                .stream()
                .map(equipoModelAssembler::toModel)
                .toList();
        CollectionModel<EntityModel<Equipo>> collectionModel = CollectionModel.of(
                entityModels,
                linkTo(methodOn(EquipoControllerV2.class).findByJuegoPrincipalId(juegoId)).withSelfRel()
        );
        return ResponseEntity.ok(collectionModel);
    }
}
