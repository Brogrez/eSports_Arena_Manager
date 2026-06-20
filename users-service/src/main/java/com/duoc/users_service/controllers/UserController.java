package com.duoc.users_service.controllers;


import com.duoc.users_service.models.User;
import com.duoc.users_service.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@Validated
@Tag(name="User V1", description = "Metodos CRUD para la gestion de usuarios")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @Operation(summary = "Listado de todos los usuarios presentes en la base de datos",
            description = "se devuelve una lista con los usuarios que se encuentran en la tabla Users de la BD")
    @ApiResponse(responseCode = "200", description = "Operacion Existosa")
    public ResponseEntity<List<User>> findAll(){
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca un user", description = "sirve para encontrar un user dentro de la BD")
    @Parameter(description = "codigo del usuario a buscar", required = true)
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "User Exitosa"),
            @ApiResponse(responseCode = "404", description = "User no se encuentra en la BD")
    })
    public ResponseEntity<User> findById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(userService.findById(id));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<User> findByEmail(@PathVariable String email){
        return ResponseEntity.status(HttpStatus.OK).body(userService.findByEmail(email));
    }

    @GetMapping("/nickname/{nickname}")
    public ResponseEntity<User> findByNickname(@PathVariable String nickname){
        return ResponseEntity.status(HttpStatus.OK).body(userService.findByNickname(nickname));
    }

    @PostMapping
    public ResponseEntity<User> save(@Valid @RequestBody User user) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.save(user));
    }
    @PutMapping("/{id}")
    public ResponseEntity<User>  update(@PathVariable Long id, @Valid @RequestBody User user) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.updateById(id, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
