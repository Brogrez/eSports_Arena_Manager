package eSports_Arena_Manager.game_service.controllers;


import eSports_Arena_Manager.game_service.models.Game;
import eSports_Arena_Manager.game_service.services.GameService;
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
    public ResponseEntity<List<Game>> findAll(){
        return ResponseEntity.status(HttpStatus.OK).body(gameService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Game> findByid(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(gameService.findById(id));
    }

    @GetMapping("/genero/{genero}")
    public ResponseEntity<Game> findByGenero(@PathVariable String genero){
        return ResponseEntity.status(HttpStatus.OK).body(gameService.findByGenero(genero));
    }

    @GetMapping("/modalidad/{modalidad}")
    public ResponseEntity<Game> findByModalidad(@PathVariable String modalidad){
        return ResponseEntity.status(HttpStatus.OK).body(gameService.findByModalidad(modalidad));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<Game> findByEstado(@PathVariable String estado){
        return ResponseEntity.status(HttpStatus.OK).body(gameService.findByEstado(estado));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Game> findByName(@PathVariable String name){
        return ResponseEntity.status(HttpStatus.OK).body(gameService.findByName(name));
    }

    @PostMapping
    public ResponseEntity<Game> save(@Valid @RequestBody Game game){
        return ResponseEntity.status(HttpStatus.CREATED).body(gameService.save(game));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Game> update(@PathVariable Long id, @Valid @RequestBody Game game){
        return ResponseEntity.status(HttpStatus.OK).body(gameService.updateById(id, game));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        gameService.deleteByid(id);
        return ResponseEntity.noContent().build();
    }
}
