package eSports_Arena_Manager.game_service.controllers;


import eSports_Arena_Manager.game_service.models.Game;
import eSports_Arena_Manager.game_service.services.GameService;
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

//GENERO MODALIDAD NAME ESTADO

@RestController
@RequestMapping("/api/v1/games")
@Validated
public class GameController {
    @Autowired
    private GameService gameService;


    @GetMapping
    @Operation(
            summary = "Listado de todos los Juegos",
            description = "Se devuelve una colección HATEOAS con los juegos enriquecidos"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<List<Game>> findAll(){
        return ResponseEntity.status(HttpStatus.OK).body(gameService.findAll());
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
    public ResponseEntity<Game> findByid(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(gameService.findById(id));
    }

    @GetMapping("/genero/{genero}")
    @Operation(
            summary = "Busqueda de juego por genero",
            description = "Se devuelve un juego segun su genero como recurso HATEOAS"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<Game> findByGenero(@PathVariable String genero){
        return ResponseEntity.status(HttpStatus.OK).body(gameService.findByGenero(genero));
    }

    @GetMapping("/modalidad/{modalidad}")
    @Operation(
            summary = "Busqueda de juego por modalidad",
            description = "Se devuelve un juego segun su modalidad como recurso HATEOAS"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<Game> findByModalidad(@PathVariable String modalidad){
        return ResponseEntity.status(HttpStatus.OK).body(gameService.findByModalidad(modalidad));
    }

    @GetMapping("/estado/{estado}")
    @Operation(
            summary = "Busqueda de juego por estado",
            description = "Se devuelve un juego segun su estado como recurso HATEOAS"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<Game> findByEstado(@PathVariable String estado){
        return ResponseEntity.status(HttpStatus.OK).body(gameService.findByEstado(estado));
    }

    @GetMapping("/name/{name}")
    @Operation(
            summary = "Busqueda de juego por nombre",
            description = "Se devuelve un juego segun su nombre como recurso HATEOAS"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<Game> findByName(@PathVariable String name){
        return ResponseEntity.status(HttpStatus.OK).body(gameService.findByName(name));
    }

    @PostMapping
    @Operation(summary = "Guardado de juego", description = "Esta es la forma de guardar un juego")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "juego a crear", required = true,
            content = @Content(schema = @Schema(implementation = Game.class))
    )
    @ApiResponse(responseCode = "201", description = "juego creado")
    public ResponseEntity<Game> save(@Valid @RequestBody Game game){
        return ResponseEntity.status(HttpStatus.CREATED).body(gameService.save(game));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizacion de juego", description = "Se actualizan los datos de un juego existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "juego actualizado"),
            @ApiResponse(responseCode = "404", description = "juego no se encuentra en la BD")
    })
    public ResponseEntity<Game> update(@PathVariable Long id, @Valid @RequestBody Game game){
        return ResponseEntity.status(HttpStatus.OK).body(gameService.updateById(id, game));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminacion de un juego", description = "Se elimina un juego por su id")
    @ApiResponse(responseCode = "204", description = "juego eliminado")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        gameService.deleteByid(id);
        return ResponseEntity.noContent().build();
    }
}
