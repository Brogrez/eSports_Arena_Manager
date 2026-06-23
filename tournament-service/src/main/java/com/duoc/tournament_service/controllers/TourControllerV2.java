package com.duoc.tournament_service.controllers;

import com.duoc.tournament_service.assemblers.TourModelAssembler;
import com.duoc.tournament_service.models.Tour;
import com.duoc.tournament_service.services.TourService;
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

import java.time.LocalDate;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v2/tournaments")
@Validated
@Tag(name = "Tournametns V2", description = "Metodos CRUD HATEOAS para la gestión de Torneos")
public class TourControllerV2 {

    @Autowired
    private TourService tourService;

    @Autowired
    private TourModelAssembler tourModelAssembler;

    @GetMapping
    @Operation(
            summary = "Listado de todos los torneos",
            description = "Se devuelve una colección HATEOAS con todos los torneos"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<CollectionModel<EntityModel<Tour>>> findAll() {
        List<EntityModel<Tour>> entityModels = this.tourService.findAll()
                .stream()
                .map(tourModelAssembler::toModel)
                .toList();
        CollectionModel<EntityModel<Tour>> collectionModel = CollectionModel.of(
                entityModels,
                linkTo(methodOn(TourControllerV2.class).findAll()).withSelfRel()
        );
        return ResponseEntity.status(HttpStatus.OK).body(collectionModel);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Busqueda de un torneo por id",
            description = "Se devuelve un torneo, en caso contrario se devuelve una excepcion"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Torneo encontrado"),
            @ApiResponse(responseCode = "404", description = "Torneo no se encuentra en la BD")
    })
    public ResponseEntity<EntityModel<Tour>> findById(
            @Parameter(description = "Id del torneo a buscar", required = true, example = "1")
            @PathVariable Long id
    ) {
        EntityModel<Tour> entityModel = this.tourModelAssembler.toModel(
                this.tourService.findById(id)
        );
        return ResponseEntity.status(HttpStatus.OK).body(entityModel);
    }

    @PostMapping
    @Operation(summary = "Guardado de torneo", description = "Esta es la forma de guardar un torneo")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Torneo a crear", required = true,
            content = @Content(schema = @Schema(implementation = Tour.class))
    )
    @ApiResponse(responseCode = "201", description = "Torneo creado")
    public ResponseEntity<EntityModel<Tour>> save(@Valid @RequestBody Tour tournament) {
        Tour tourCreado = this.tourService.save(tournament);
        EntityModel<Tour> entityModel = this.tourModelAssembler.toModel(tourCreado);
        return ResponseEntity.status(HttpStatus.CREATED).body(entityModel);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizacion de torneo", description = "Se actualizan los datos de un torneo existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Torneo actualizado"),
            @ApiResponse(responseCode = "404", description = "Torneo no se encuentra en la BD")
    })
    public ResponseEntity<EntityModel<Tour>> updateById(
            @Parameter(description = "Id del torneo a actualizar", required = true, example = "1")
            @PathVariable Long id,
            @Valid @RequestBody Tour tournament
    ) {
        Tour tourUpdate = this.tourService.updateById(id, tournament);
        EntityModel<Tour> entityModel = this.tourModelAssembler.toModel(tourUpdate);
        return ResponseEntity.status(HttpStatus.OK).body(entityModel);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminacion de torneo", description = "Se elimina un torneo por su id")
    @ApiResponse(responseCode = "204", description = "Torneo eliminado")
    public ResponseEntity<Void> deleteById(
            @Parameter(description = "Id del torneo a eliminar", required = true, example = "1")
            @PathVariable Long id
    ) {
        this.tourService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/game/{gameId}")
    @Operation(
            summary = "Listado de torneos por juego",
            description = "Se devuelven los torneos asociados a un juego como colección HATEOAS"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<CollectionModel<EntityModel<Tour>>> findByGameId(
            @Parameter(description = "Id del juego", required = true, example = "1")
            @PathVariable Long gameId
    ) {
        List<EntityModel<Tour>> entityModels = this.tourService.findByGameId(gameId)
                .stream()
                .map(tourModelAssembler::toModel)
                .toList();
        CollectionModel<EntityModel<Tour>> collectionModel = CollectionModel.of(
                entityModels,
                linkTo(methodOn(TourControllerV2.class).findByGameId(gameId)).withSelfRel()
        );
        return ResponseEntity.status(HttpStatus.OK).body(collectionModel);
    }

    @GetMapping("/estado/{estado}")
    @Operation(
            summary = "Busqueda de torneos por estado",
            description = "Se devuelven los torneos segun su estado como colección HATEOAS"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<CollectionModel<EntityModel<Tour>>> findByEstado(
            @Parameter(description = "Estado del torneo", required = true, example = "EN_CURSO")
            @PathVariable String estado
    ) {
        List<EntityModel<Tour>> entityModels = this.tourService.findByEstado(estado)
                .stream()
                .map(tourModelAssembler::toModel)
                .toList();
        CollectionModel<EntityModel<Tour>> collectionModel = CollectionModel.of(
                entityModels,
                linkTo(methodOn(TourControllerV2.class).findByEstado(estado)).withSelfRel()
        );
        return ResponseEntity.status(HttpStatus.OK).body(collectionModel);
    }

    @GetMapping("/fecha/{fechaInicio}")
    @Operation(
            summary = "Busqueda de torneos por fecha de inicio",
            description = "Se devuelven los torneos segun su fecha de inicio como colección HATEOAS"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<CollectionModel<EntityModel<Tour>>> findByFechaInicio(
            @Parameter(description = "Fecha de inicio del torneo", required = true, example = "2026-08-01")
            @PathVariable LocalDate fechaInicio
    ) {
        List<EntityModel<Tour>> entityModels = this.tourService.findByFechaInicio(fechaInicio)
                .stream()
                .map(tourModelAssembler::toModel)
                .toList();
        CollectionModel<EntityModel<Tour>> collectionModel = CollectionModel.of(
                entityModels,
                linkTo(methodOn(TourControllerV2.class).findByFechaInicio(fechaInicio)).withSelfRel()
        );
        return ResponseEntity.status(HttpStatus.OK).body(collectionModel);
    }

    @PatchMapping("/{id}/cancelar")
    @Operation(summary = "Cancelacion de torneo", description = "Se cancela un torneo existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Torneo cancelado"),
            @ApiResponse(responseCode = "404", description = "Torneo no se encuentra en la BD")
    })
    public ResponseEntity<EntityModel<Tour>> cancelar(
            @Parameter(description = "Id del torneo a cancelar", required = true, example = "1")
            @PathVariable Long id
    ) {
        EntityModel<Tour> entityModel = this.tourModelAssembler.toModel(
                this.tourService.cancelar(id)
        );
        return ResponseEntity.status(HttpStatus.OK).body(entityModel);
    }

    @PatchMapping("/{id}/cerrar")
    @Operation(summary = "Cierre de torneo", description = "Se cierra un torneo existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Torneo cerrado"),
            @ApiResponse(responseCode = "404", description = "Torneo no se encuentra en la BD")
    })
    public ResponseEntity<EntityModel<Tour>> cerrar(
            @Parameter(description = "Id del torneo a cerrar", required = true, example = "1")
            @PathVariable Long id
    ) {
        EntityModel<Tour> entityModel = this.tourModelAssembler.toModel(
                this.tourService.cerrar(id)
        );
        return ResponseEntity.status(HttpStatus.OK).body(entityModel);
    }


}
