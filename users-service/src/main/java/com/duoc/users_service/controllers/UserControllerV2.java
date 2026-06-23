package com.duoc.users_service.controllers;

import com.duoc.users_service.assemblers.UserModelAssembler;
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
@RequestMapping("/api/v2/users")
@Validated
@Tag(name = "Users V2", description = "Metodos CRUD HATEOAS para la gestion de usuarios")
public class UserControllerV2 {

    @Autowired
    private UserService userService;


    @Autowired
    private UserModelAssembler userModelAssembler;


    @GetMapping
    @Operation(
            summary = "Listado de todos los usuarios",
            description = "Se devuelve una colección HATEOAS con todos los usuarios registrados en la BD"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<CollectionModel<EntityModel<User>>> findAll() {
        List<EntityModel<User>> entityModels = this.userService.findAll()
                .stream()
                .map(userModelAssembler::toModel)
                .toList();
        CollectionModel<EntityModel<User>> collectionModel = CollectionModel.of(
                entityModels,
                linkTo(methodOn(UserControllerV2.class).findAll()).withSelfRel()
        );
        return ResponseEntity.ok(collectionModel);
    }


    @GetMapping("/{id}")
    @Operation(
            summary = "Busqueda de un usuario por id",
            description = "Se devuelve un usuario, en caso contrario se devuelve una excepcion"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuario no se encuentra en la BD")
    })
    public ResponseEntity<EntityModel<User>> findById(
            @Parameter(description = "Id del usuario a buscar", required = true, example = "1")
            @PathVariable Long id
    ) {
        EntityModel<User> entityModel = this.userModelAssembler.toModel(
                this.userService.findById(id)
        );
        return ResponseEntity.ok(entityModel);
    }


    @GetMapping("/email/{email}")
    @Operation(
            summary = "Busqueda de usuario por email",
            description = "Se devuelve el usuario asociado a un email especifico como recurso HATEOAS"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<EntityModel<User>> findByEmail(
            @Parameter(description = "Email del usuario a buscar", required = true, example = "usuario@correo.com")
            @PathVariable String email
    ) {
        EntityModel<User> entityModel = this.userModelAssembler.toModel(
                this.userService.findByEmail(email)
        );
        return ResponseEntity.ok(entityModel);
    }


    @GetMapping("/nickname/{nickname}")
    @Operation(
            summary = "Busqueda de usuario por nickname",
            description = "Se devuelve el usuario asociado a un nickname especifico como recurso HATEOAS"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<EntityModel<User>> findByNickname(
            @Parameter(description = "Nickname del usuario a buscar", required = true, example = "juanperez123")
            @PathVariable String nickname
    ) {
        EntityModel<User> entityModel = this.userModelAssembler.toModel(
                this.userService.findByNickname(nickname)
        );
        return ResponseEntity.ok(entityModel);
    }


    @PostMapping
    @Operation(summary = "Guardado de usuario", description = "Esta es la forma de guardar un usuario")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Usuario a crear", required = true,
            content = @Content(schema = @Schema(implementation = User.class))
    )
    @ApiResponse(responseCode = "201", description = "Usuario creado")
    public ResponseEntity<EntityModel<User>> save(@Valid @RequestBody User user) {
        User userCreate = this.userService.save(user);
        EntityModel<User> entityModel = this.userModelAssembler.toModel(userCreate);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(entityModel);
    }


    @PutMapping("/{id}")
    @Operation(summary = "Actualizacion de usuario", description = "Se actualizan los datos de un usuario existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado"),
            @ApiResponse(responseCode = "404", description = "Usuario no se encuentra en la BD")
    })
    public ResponseEntity<EntityModel<User>> update(
            @Parameter(description = "Id del usuario a actualizar", required = true, example = "1")
            @PathVariable Long id,
            @Valid @RequestBody User user
    ) {
        User userUpdate = this.userService.updateById(id, user);
        EntityModel<User> entityModel = this.userModelAssembler.toModel(userUpdate);
        return ResponseEntity.ok(entityModel);
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminacion de usuario", description = "Se elimina un usuario por su id")
    @ApiResponse(responseCode = "204", description = "Usuario eliminado")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Id del usuario a eliminar", required = true, example = "1")
            @PathVariable Long id
    ) {
        this.userService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}