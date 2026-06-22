package com.duoc.users_service.controllers;


import com.duoc.users_service.models.User;
import com.duoc.users_service.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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

// @RestController: cada metodo devuelve datos (JSON), no vistas HTML.
// @RequestMapping: prefijo comun de las rutas (version 1 de la API).
// @Validated: activa la validacion de los @Valid. @Tag: agrupa los endpoints en Swagger.
@RestController
@RequestMapping("/api/v1/users")
@Validated
@Tag(name = "Users V1", description = "Metodos CRUD para la gestion de usuarios")
public class UserController {

    @Autowired
    private UserService userService;

    // findAll devuelve la lista de usuarios presentes en la base de datos.
    @GetMapping
    @Operation(
            summary = "Listado de todos los usuarios",
            description = "Se devuelve una lista con todos los usuarios registrados en la BD"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Busqueda de un usuario por id",
            description = "Se devuelve un usuario, en caso contrario se devuelve una excepcion"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuario encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no se encuentra en la BD")
    })
    public ResponseEntity<User> findById(
            @Parameter(description = "Id del usuario a buscar", required = true, example = "1")
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping("/email/{email}")
    @Operation(
            summary = "Busqueda de usuario por email",
            description = "Se devuelve el usuario asociado a un email especifico"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<User> findByEmail(
            @Parameter(description = "Email del usuario a buscar", required = true, example = "usuario@correo.com")
            @PathVariable String email
    ) {
        return ResponseEntity.ok(userService.findByEmail(email));
    }

    @GetMapping("/nickname/{nickname}")
    @Operation(
            summary = "Busqueda de usuario por nickname",
            description = "Se devuelve el usuario asociado a un nickname especifico"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<User> findByNickname(
            @Parameter(description = "Nickname del usuario a buscar", required = true, example = "juanperez123")
            @PathVariable String nickname
    ) {
        return ResponseEntity.ok(userService.findByNickname(nickname));
    }

    // POST /api/v1/users => crear. @Valid valida el body; @RequestBody convierte el JSON.
    @PostMapping
    @Operation(summary = "Guardado de usuario", description = "Esta es la forma de guardar un usuario")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Usuario a crear", required = true,
            content = @Content(schema = @Schema(implementation = User.class))
    )
    @ApiResponse(responseCode = "201", description = "Usuario creado")
    public ResponseEntity<User> save(@Valid @RequestBody User user) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.save(user));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizacion de usuario", description = "Se actualizan los datos de un usuario existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado"),
            @ApiResponse(responseCode = "404", description = "Usuario no se encuentra en la BD")
    })
    public ResponseEntity<User> update(
            @Parameter(description = "Id del usuario a actualizar", required = true, example = "1")
            @PathVariable Long id,
            @Valid @RequestBody User user
    ) {
        return ResponseEntity.ok(userService.updateById(id, user));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminacion de usuario", description = "Se elimina un usuario por su id")
    @ApiResponse(responseCode = "204", description = "Usuario eliminado")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Id del usuario a eliminar", required = true, example = "1")
            @PathVariable Long id
    ) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}