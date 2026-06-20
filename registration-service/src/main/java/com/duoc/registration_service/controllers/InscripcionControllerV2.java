package com.duoc.registration_service.controllers;

import com.duoc.registration_service.assamblers.InscripcionModelAssembler;
import com.duoc.registration_service.models.Inscripcion;
import com.duoc.registration_service.models.dtos.InscripcionDTO;
import com.duoc.registration_service.services.InscripcionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
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
@RequestMapping("/api/v2/inscripciones")
@Validated
@Tag(name = "Inscripciones V2", description = "Metodos CRUD HATEOAS para la gestión de inscripciones")
public class InscripcionControllerV2 {

    @Autowired
    private InscripcionService inscripcionService;

    // El assembler arma los enlaces HATEOAS de cada inscripcion (ver InscripcionModelAssembler).
    @Autowired
    private InscripcionModelAssembler inscripcionModelAssembler;

    /**
     * Crea una nueva inscripcion.
     *
     * @param inscripcion datos de la inscripcion a crear (validados)
     * @return la inscripcion creada como recurso HATEOAS
     */
    @PostMapping
    @Operation(summary = "Guardado de inscripcion", description = "Esta es la forma de guardar una inscripcion")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Inscripcion a crear", required = true,
            content = @Content(schema = @Schema(implementation = Inscripcion.class))
    )
    @ApiResponse(responseCode = "201", description = "Inscripcion creada")
    public ResponseEntity<EntityModel<Inscripcion>> save(@Valid @RequestBody Inscripcion inscripcion) {
        Inscripcion inscripcionCreate = this.inscripcionService.save(inscripcion);
        EntityModel<Inscripcion> entityModel = this.inscripcionModelAssembler.toModel(inscripcionCreate);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(entityModel);
    }


    @GetMapping
    @Operation(
            summary = "Listado de todas las inscripciones",
            description = "Se devuelve una colección HATEOAS con las inscripciones en formato DTO"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<CollectionModel<InscripcionDTO>> findAll() {

        List<InscripcionDTO> inscripciones = this.inscripcionService.findAll();
        CollectionModel<InscripcionDTO> collectionModel = CollectionModel.of(
                inscripciones,
                linkTo(methodOn(InscripcionControllerV2.class).findAll()).withSelfRel()
        );
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(collectionModel);
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
    public ResponseEntity<EntityModel<Inscripcion>> findById(
            @Parameter(description = "Id de la inscripcion a buscar", required = true, example = "1")
            @PathVariable Long id
    ) {
        EntityModel<Inscripcion> entityModel = this.inscripcionModelAssembler.toModel(
                this.inscripcionService.findByInscripcionId(id)
        );
        return ResponseEntity.ok(entityModel);
    }

    @GetMapping("/torneo/{torneoId}")
    @Operation(
            summary = "Listado de inscripciones por torneo",
            description = "Se devuelven las inscripciones asociadas a un torneo como colección HATEOAS"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<CollectionModel<EntityModel<Inscripcion>>> findByTorneoId(
            @Parameter(description = "Id del torneo", required = true, example = "1")
            @PathVariable Long torneoId
    ) {
        List<EntityModel<Inscripcion>> entityModels = List.of(this.inscripcionService.findByTorneoId(torneoId))
                .stream()
                .map(inscripcionModelAssembler::toModel)
                .toList();
        CollectionModel<EntityModel<Inscripcion>> collectionModel = CollectionModel.of(
                entityModels,
                linkTo(methodOn(InscripcionControllerV2.class).findByTorneoId(torneoId)).withSelfRel()
        );
        return ResponseEntity.ok(collectionModel);
    }


    @GetMapping("/jugador/{jugadorId}")
    @Operation(
            summary = "Listado de inscripciones por jugador",
            description = "Se devuelven las inscripciones asociadas a un jugador como colección HATEOAS"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<CollectionModel<EntityModel<Inscripcion>>> findByJugadorId(
            @Parameter(description = "Id del jugador", required = true, example = "1")
            @PathVariable Long jugadorId
    ) {
        List<EntityModel<Inscripcion>> entityModels = List.of(this.inscripcionService.findByJugadorId(jugadorId))
                .stream()
                .map(inscripcionModelAssembler::toModel)
                .toList();
        CollectionModel<EntityModel<Inscripcion>> collectionModel = CollectionModel.of(
                entityModels,
                linkTo(methodOn(InscripcionControllerV2.class).findByJugadorId(jugadorId)).withSelfRel()
        );
        return ResponseEntity.ok(collectionModel);
    }

    /**
     * Lista las inscripciones asociadas a un equipo.
     *
     * @param equipoId identificador del equipo
     * @return colección HATEOAS de inscripciones del equipo
     */
    @GetMapping("/equipo/{equipoId}")
    @Operation(
            summary = "Listado de inscripciones por equipo",
            description = "Se devuelven las inscripciones asociadas a un equipo como colección HATEOAS"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<CollectionModel<EntityModel<Inscripcion>>> findByEquipoId(
            @Parameter(description = "Id del equipo", required = true, example = "1")
            @PathVariable Long equipoId
    ) {
        List<EntityModel<Inscripcion>> entityModels = List.of(this.inscripcionService.findByEquipoId(equipoId))
                .stream()
                .map(inscripcionModelAssembler::toModel)
                .toList();
        CollectionModel<EntityModel<Inscripcion>> collectionModel = CollectionModel.of(
                entityModels,
                linkTo(methodOn(InscripcionControllerV2.class).findByEquipoId(equipoId)).withSelfRel()
        );
        return ResponseEntity.ok(collectionModel);
    }


    @PutMapping("/{id}")
    @Operation(summary = "Actualizacion de inscripcion", description = "Se actualizan los datos de una inscripcion existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inscripcion actualizada"),
            @ApiResponse(responseCode = "404", description = "Inscripcion no se encuentra en la BD")
    })
    public ResponseEntity<EntityModel<Inscripcion>> update(
            @Parameter(description = "Id de la inscripcion a actualizar", required = true, example = "1")
            @PathVariable Long id,
            @Valid @RequestBody Inscripcion inscripcion
    ) {
        Inscripcion inscripcionUpdate = this.inscripcionService.updateById(id, inscripcion);
        EntityModel<Inscripcion> entityModel = this.inscripcionModelAssembler.toModel(inscripcionUpdate);
        return ResponseEntity.ok(entityModel);
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminacion de inscripcion", description = "Se elimina una inscripcion por su id")
    @ApiResponse(responseCode = "204", description = "Inscripcion eliminada")
    public ResponseEntity<Void> deleteById(
            @Parameter(description = "Id de la inscripcion a eliminar", required = true, example = "1")
            @PathVariable Long id
    ) {
        this.inscripcionService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}