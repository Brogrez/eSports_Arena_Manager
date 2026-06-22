package com.example.match_service.controllers;

import com.example.match_service.assemblers.MatchModelAssembler;
import com.example.match_service.models.Match;
import com.example.match_service.services.MatchService;
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
@RequestMapping("/api/v2/matchs")
@Validated
@Tag(name = "Matchs V2", description = "Metodos CRUD HATEOAS para la gestión de partidas")
public class MatchControllerV2 {

    @Autowired
    private MatchService matchService;

    @Autowired
    private MatchModelAssembler matchModelAssembler;

    @GetMapping
    @Operation(
            summary = "Listado de todas las partidas",
            description = "Se devuelve una colección HATEOAS con todas las partidas"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<CollectionModel<EntityModel<Match>>> findAll() {
        List<EntityModel<Match>> entityModels = this.matchService.findAll()
                .stream()
                .map(matchModelAssembler::toModel)
                .toList();
        CollectionModel<EntityModel<Match>> collectionModel = CollectionModel.of(
                entityModels,
                linkTo(methodOn(MatchControllerV2.class).findAll()).withSelfRel()
        );
        return ResponseEntity.status(HttpStatus.OK).body(collectionModel);
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
    public ResponseEntity<EntityModel<Match>> findById(
            @Parameter(description = "Id de la partida a buscar", required = true, example = "1")
            @PathVariable Long id
    ) {
        EntityModel<Match> entityModel = this.matchModelAssembler.toModel(
                this.matchService.findById(id)
        );
        return ResponseEntity.status(HttpStatus.OK).body(entityModel);
    }

    @PostMapping
    @Operation(summary = "Guardado de partida", description = "Esta es la forma de guardar una partida")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Partida a crear", required = true,
            content = @Content(schema = @Schema(implementation = Match.class))
    )
    @ApiResponse(responseCode = "201", description = "Partida creada")
    public ResponseEntity<EntityModel<Match>> save(@Valid @RequestBody Match match) {
        Match matchCreado = this.matchService.save(match);
        EntityModel<Match> entityModel = this.matchModelAssembler.toModel(matchCreado);
        return ResponseEntity.status(HttpStatus.CREATED).body(entityModel);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizacion de partida", description = "Se actualizan los datos de una partida existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Partida actualizada"),
            @ApiResponse(responseCode = "404", description = "Partida no se encuentra en la BD")
    })
    public ResponseEntity<EntityModel<Match>> updateById(
            @Parameter(description = "Id de la partida a actualizar", required = true, example = "1")
            @PathVariable Long id,
            @Valid @RequestBody Match match
    ) {
        Match matchUpdate = this.matchService.updateById(match, id);
        EntityModel<Match> entityModel = this.matchModelAssembler.toModel(matchUpdate);
        return ResponseEntity.status(HttpStatus.OK).body(entityModel);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminacion de partida", description = "Se elimina una partida por su id")
    @ApiResponse(responseCode = "204", description = "Partida eliminada")
    public ResponseEntity<Void> deleteById(
            @Parameter(description = "Id de la partida a eliminar", required = true, example = "1")
            @PathVariable Long id
    ) {
        this.matchService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/tour/{tourId}")
    @Operation(
            summary = "Listado de partidas por torneo",
            description = "Se devuelven las partidas asociadas a un torneo como colección HATEOAS"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<CollectionModel<EntityModel<Match>>> findByTorneoId(
            @Parameter(description = "Id del torneo", required = true, example = "1")
            @PathVariable Long tourId
    ) {
        List<EntityModel<Match>> entityModels = this.matchService.findByTorneoId(tourId)
                .stream()
                .map(matchModelAssembler::toModel)
                .toList();
        CollectionModel<EntityModel<Match>> collectionModel = CollectionModel.of(
                entityModels,
                linkTo(methodOn(MatchControllerV2.class).findByTorneoId(tourId)).withSelfRel()
        );
        return ResponseEntity.status(HttpStatus.OK).body(collectionModel);
    }

    @GetMapping("/estado/{estado}")
    @Operation(
            summary = "Busqueda de partidas por estado",
            description = "Se devuelven las partidas segun su estado como colección HATEOAS"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<CollectionModel<EntityModel<Match>>> findByEstado(
            @Parameter(description = "Estado de la partida", required = true, example = "PROGRAMADA")
            @PathVariable String estado
    ) {
        List<EntityModel<Match>> entityModels = this.matchService.findByEstado(estado)
                .stream()
                .map(matchModelAssembler::toModel)
                .toList();
        CollectionModel<EntityModel<Match>> collectionModel = CollectionModel.of(
                entityModels,
                linkTo(methodOn(MatchControllerV2.class).findByEstado(estado)).withSelfRel()
        );
        return ResponseEntity.status(HttpStatus.OK).body(collectionModel);
    }

    @GetMapping("/round/{round}")
    @Operation(
            summary = "Busqueda de partidas por ronda",
            description = "Se devuelven las partidas segun su ronda como colección HATEOAS"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<CollectionModel<EntityModel<Match>>> findByRonda(
            @Parameter(description = "Ronda de la partida", required = true, example = "Cuartos de final")
            @PathVariable String round
    ) {
        List<EntityModel<Match>> entityModels = this.matchService.findByRonda(round)
                .stream()
                .map(matchModelAssembler::toModel)
                .toList();
        CollectionModel<EntityModel<Match>> collectionModel = CollectionModel.of(
                entityModels,
                linkTo(methodOn(MatchControllerV2.class).findByRonda(round)).withSelfRel()
        );
        return ResponseEntity.status(HttpStatus.OK).body(collectionModel);
    }

    @PatchMapping("/{id}/cancelar")
    @Operation(summary = "Cancelacion de partida", description = "Se cancela una partida existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Partida cancelada"),
            @ApiResponse(responseCode = "404", description = "Partida no se encuentra en la BD")
    })
    public ResponseEntity<EntityModel<Match>> cancelar(
            @Parameter(description = "Id de la partida a cancelar", required = true, example = "1")
            @PathVariable Long id
    ) {
        EntityModel<Match> entityModel = this.matchModelAssembler.toModel(
                this.matchService.cancelar(id)
        );
        return ResponseEntity.status(HttpStatus.OK).body(entityModel);
    }

}
