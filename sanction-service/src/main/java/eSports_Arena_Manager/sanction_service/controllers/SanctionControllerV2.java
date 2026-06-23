package eSports_Arena_Manager.sanction_service.controllers;

import eSports_Arena_Manager.sanction_service.assemblers.SanctionModelAssembler;
import eSports_Arena_Manager.sanction_service.models.Sanction;
import eSports_Arena_Manager.sanction_service.services.SanctionService;
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
@RequestMapping("/api/v2/sanctions")
@Validated
@Tag(name = "Sanctions V2", description = "Metodos CRUD HATEOAS para la gestión de sanciones")
public class SanctionControllerV2 {

    @Autowired
    private SanctionService sanctionService;


    @Autowired
    private SanctionModelAssembler sanctionModelAssembler;


    @GetMapping
    @Operation(
            summary = "Listado de todas las sanciones",
            description = "Se devuelve una colección HATEOAS con todas las sanciones registradas en la BD"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<CollectionModel<EntityModel<Sanction>>> findAll() {
        List<EntityModel<Sanction>> entityModels = this.sanctionService.findAll()
                .stream()
                .map(sanctionModelAssembler::toModel)
                .toList();
        CollectionModel<EntityModel<Sanction>> collectionModel = CollectionModel.of(
                entityModels,
                linkTo(methodOn(SanctionControllerV2.class).findAll()).withSelfRel()
        );
        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Busqueda de una sancion por id",
            description = "Se devuelve una sancion, en caso contrario se devuelve una excepcion"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sancion encontrada"),
            @ApiResponse(responseCode = "404", description = "Sancion no se encuentra en la BD")
    })
    public ResponseEntity<EntityModel<Sanction>> findById(
            @Parameter(description = "Id de la sancion a buscar", required = true, example = "1")
            @PathVariable Long id
    ) {
        EntityModel<Sanction> entityModel = this.sanctionModelAssembler.toModel(
                this.sanctionService.findById(id)
        );
        return ResponseEntity.ok(entityModel);
    }


    @PostMapping
    @Operation(summary = "Guardado de sancion", description = "Esta es la forma de guardar una sancion")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Sancion a crear", required = true,
            content = @Content(schema = @Schema(implementation = Sanction.class))
    )
    @ApiResponse(responseCode = "201", description = "Sancion creada")
    public ResponseEntity<EntityModel<Sanction>> save(@Valid @RequestBody Sanction sanction) {
        Sanction sanctionCreate = this.sanctionService.save(sanction);
        EntityModel<Sanction> entityModel = this.sanctionModelAssembler.toModel(sanctionCreate);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(entityModel);
    }


    @PutMapping("/{id}")
    @Operation(summary = "Actualizacion de sancion", description = "Se actualizan los datos de una sancion existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sancion actualizada"),
            @ApiResponse(responseCode = "404", description = "Sancion no se encuentra en la BD")
    })
    public ResponseEntity<EntityModel<Sanction>> updateById(
            @Parameter(description = "Id de la sancion a actualizar", required = true, example = "1")
            @PathVariable Long id,
            @Valid @RequestBody Sanction sanction
    ) {
        Sanction sanctionUpdate = this.sanctionService.updateById(id, sanction);
        EntityModel<Sanction> entityModel = this.sanctionModelAssembler.toModel(sanctionUpdate);
        return ResponseEntity.ok(entityModel);
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminacion de sancion", description = "Se elimina una sancion por su id")
    @ApiResponse(responseCode = "204", description = "Sancion eliminada")
    public ResponseEntity<Void> deleteById(
            @Parameter(description = "Id de la sancion a eliminar", required = true, example = "1")
            @PathVariable Long id
    ) {
        this.sanctionService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/user/{userId}")
    @Operation(
            summary = "Listado de sanciones por usuario",
            description = "Se devuelven las sanciones asociadas a un usuario como colección HATEOAS"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<CollectionModel<EntityModel<Sanction>>> findByUsuarioId(
            @Parameter(description = "Id del usuario", required = true, example = "1")
            @PathVariable Long userId
    ) {
        List<EntityModel<Sanction>> entityModels = this.sanctionService.findByUsuarioId(userId)
                .stream()
                .map(sanctionModelAssembler::toModel)
                .toList();
        CollectionModel<EntityModel<Sanction>> collectionModel = CollectionModel.of(
                entityModels,
                linkTo(methodOn(SanctionControllerV2.class).findByUsuarioId(userId)).withSelfRel()
        );
        return ResponseEntity.ok(collectionModel);
    }


    @GetMapping("/team/{teamId}")
    @Operation(
            summary = "Listado de sanciones por equipo",
            description = "Se devuelven las sanciones asociadas a un equipo como colección HATEOAS"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<CollectionModel<EntityModel<Sanction>>> findByTeamId(
            @Parameter(description = "Id del equipo", required = true, example = "1")
            @PathVariable Long teamId
    ) {
        List<EntityModel<Sanction>> entityModels = this.sanctionService.findByTeamId(teamId)
                .stream()
                .map(sanctionModelAssembler::toModel)
                .toList();
        CollectionModel<EntityModel<Sanction>> collectionModel = CollectionModel.of(
                entityModels,
                linkTo(methodOn(SanctionControllerV2.class).findByTeamId(teamId)).withSelfRel()
        );
        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/estado/{estado}")
    @Operation(
            summary = "Listado de sanciones por estado",
            description = "Se devuelven las sanciones filtradas por estado como colección HATEOAS"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<CollectionModel<EntityModel<Sanction>>> findByEstado(
            @Parameter(description = "Estado de la sancion a buscar", required = true, example = "ACTIVA")
            @PathVariable String estado
    ) {
        List<EntityModel<Sanction>> entityModels = this.sanctionService.findByEstado(estado)
                .stream()
                .map(sanctionModelAssembler::toModel)
                .toList();
        CollectionModel<EntityModel<Sanction>> collectionModel = CollectionModel.of(
                entityModels,
                linkTo(methodOn(SanctionControllerV2.class).findByEstado(estado)).withSelfRel()
        );
        return ResponseEntity.ok(collectionModel);
    }


    @PatchMapping("/{id}/cerrar")
    @Operation(
            summary = "Cierre de sancion",
            description = "Actualiza el estado de una sancion especifica marcandola como cerrada (HATEOAS)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sancion cerrada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Sancion no se encuentra en la BD")
    })
    public ResponseEntity<EntityModel<Sanction>> cerrar(
            @Parameter(description = "Id de la sancion a cerrar", required = true, example = "1")
            @PathVariable Long id
    ) {
        Sanction sanctionCerrada = this.sanctionService.cerrar(id);
        EntityModel<Sanction> entityModel = this.sanctionModelAssembler.toModel(sanctionCerrada);
        return ResponseEntity.ok(entityModel);
    }
}