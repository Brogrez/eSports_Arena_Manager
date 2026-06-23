package eSports_Arena_Manager.game_service.controllers;

import eSports_Arena_Manager.game_service.assemblers.GameAssembler;
import eSports_Arena_Manager.game_service.models.Game;
import eSports_Arena_Manager.game_service.services.GameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@RequestMapping("/api/v2/games")
@Validated
public class GameControllerV2 {
    @Autowired
    private GameService gameService;
    @Autowired
    private GameAssembler gameAssembler;

    @GetMapping
    @Operation(
            summary = "Listado de todos los Juegos",
            description = "Se devuelve una colección HATEOAS con los juegos enriquecidos"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<CollectionModel<Game>> findAll() {

        List<Game> miembros = this.gameService.findAll();
        CollectionModel<Game> collectionModel = CollectionModel.of(
                miembros,
                linkTo(methodOn(GameControllerV2.class).findAll()).withSelfRel()
        );
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(collectionModel);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Busqueda de un juego por id",
            description = "Se devuelve un juego, en caso contrario se devuelve una excepcion"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "juego encontrado"),
            @ApiResponse(responseCode = "404", description = "juego no se encuentra en la BD")
    })
    public ResponseEntity<EntityModel<Game>> findById(
            @Parameter(description = "Id del juego a buscar", required = true, example = "1")
            @PathVariable Long id
    ) {
        EntityModel<Game> entityModel = this.gameAssembler.toModel(
                this.gameService.findById(id)
        );
        return ResponseEntity.ok(entityModel);
    }

    @PostMapping
    @Operation(summary = "Guardado de juego", description = "Esta es la forma de guardar un juego")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "juego a crear", required = true,
            content = @Content(schema = @Schema(implementation = Game.class))
    )
    @ApiResponse(responseCode = "201", description = "juego creado")
    public ResponseEntity<EntityModel<Game>> save(@Valid @RequestBody Game game) {
        Game gameCreate = this.gameService.save(game);
        EntityModel<Game> entityModel = this.gameAssembler.toModel(gameCreate);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(entityModel);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizacion de juego", description = "Se actualizan los datos de un juego existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "juego actualizado"),
            @ApiResponse(responseCode = "404", description = "juego no se encuentra en la BD")
    })
    public ResponseEntity<EntityModel<Game>> update(
            @Parameter(description = "Id del miembro a actualizar", required = true, example = "1")
            @PathVariable Long id,
            @Valid @RequestBody Game game
    ) {
        Game gameUpdate = this.gameService.updateById(id, game);
        EntityModel<Game> entityModel = this.gameAssembler.toModel(gameUpdate);
        return ResponseEntity.ok(entityModel);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminacion de un juego", description = "Se elimina un juego por su id")
    @ApiResponse(responseCode = "204", description = "juego eliminado")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Id del juego a eliminar", required = true, example = "1")
            @PathVariable Long id
    ) {
        this.gameService.deleteByid(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/genero/{genero}")
    @Operation(
            summary = "Busqueda de juego por genero",
            description = "Se devuelve un juego segun su genero como recurso HATEOAS"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<EntityModel<Game>> findByGenero(
            @Parameter(description = "Genero del juego", required = true, example = "FPS")
            @PathVariable String genero
    ) {
        EntityModel<Game> entityModel = this.gameAssembler.toModel(
                this.gameService.findByGenero(genero)
        );
        return ResponseEntity.ok(entityModel);
    }

    @GetMapping("/modalidad/{modalidad}")
    @Operation(
            summary = "Busqueda de juego por modalidad",
            description = "Se devuelve un juego segun su modalidad como recurso HATEOAS"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<EntityModel<Game>> findByModalidad(
            @Parameter(description = "Modalidad del juego", required = true, example = "5v5")
            @PathVariable String modalidad
    ) {
        EntityModel<Game> entityModel = this.gameAssembler.toModel(
                this.gameService.findByModalidad(modalidad)
        );
        return ResponseEntity.ok(entityModel);
    }

    @GetMapping("/estado/{estado}")
    @Operation(
            summary = "Busqueda de juego por estado",
            description = "Se devuelve un juego segun su estado como recurso HATEOAS"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<EntityModel<Game>> findByEstado(
            @Parameter(description = "Estado del juego", required = true, example = "ACTIVO")
            @PathVariable String estado
    ) {
        EntityModel<Game> entityModel = this.gameAssembler.toModel(
                this.gameService.findByEstado(estado)
        );
        return ResponseEntity.ok(entityModel);
    }

    @GetMapping("/name/{name}")
    @Operation(
            summary = "Busqueda de juego por nombre",
            description = "Se devuelve un juego segun su nombre como recurso HATEOAS"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<EntityModel<Game>> findByName(
            @Parameter(description = "Nombre del juego", required = true, example = "Valorant")
            @PathVariable String name
    ) {
        EntityModel<Game> entityModel = this.gameAssembler.toModel(
                this.gameService.findByName(name)
        );
        return ResponseEntity.ok(entityModel);
    }




}

