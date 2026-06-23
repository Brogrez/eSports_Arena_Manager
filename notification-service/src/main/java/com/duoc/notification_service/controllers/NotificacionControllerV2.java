package com.duoc.notification_service.controllers;

import com.duoc.notification_service.assemblers.NotificacionModelAssembler;
import com.duoc.notification_service.models.Notificacion;
import com.duoc.notification_service.services.NotificacionService;
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
@RequestMapping("/api/v2/notificaciones")
@Validated
@Tag(name = "Notificaciones V2", description = "Metodos CRUD HATEOAS para la gestión de notificaciones")
public class NotificacionControllerV2 {

    @Autowired
    private NotificacionService notificacionService;

    // El assembler arma los enlaces HATEOAS de cada notificacion (ver NotificacionModelAssembler).
    @Autowired
    private NotificacionModelAssembler notificacionModelAssembler;

    /**
     * Lista todas las notificaciones.
     *
     * @return colección HATEOAS de notificaciones
     */
    @GetMapping
    @Operation(
            summary = "Listado de todas las notificaciones",
            description = "Se devuelve una colección HATEOAS con todas las notificaciones registradas"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<CollectionModel<EntityModel<Notificacion>>> findAll() {
        List<EntityModel<Notificacion>> entityModels = this.notificacionService.findAll()
                .stream()
                .map(notificacionModelAssembler::toModel)
                .toList();
        CollectionModel<EntityModel<Notificacion>> collectionModel = CollectionModel.of(
                entityModels,
                linkTo(methodOn(NotificacionControllerV2.class).findAll()).withSelfRel()
        );
        return ResponseEntity.ok(collectionModel);
    }

    /**
     * Busca una notificacion por su identificador.
     *
     * @param notificacionId identificador de la notificacion
     * @return la notificacion encontrada como recurso HATEOAS
     */
    @GetMapping("/{notificacionId}")
    @Operation(
            summary = "Busqueda de una notificacion por id",
            description = "Se devuelve una notificacion, en caso contrario se devuelve una excepcion"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notificacion encontrada"),
            @ApiResponse(responseCode = "404", description = "Notificacion no se encuentra en la BD")
    })
    public ResponseEntity<EntityModel<Notificacion>> findByNotificacionId(
            @Parameter(description = "Id de la notificacion a buscar", required = true, example = "1")
            @PathVariable Long notificacionId
    ) {
        EntityModel<Notificacion> entityModel = this.notificacionModelAssembler.toModel(
                this.notificacionService.findBynotificacionId(notificacionId)
        );
        return ResponseEntity.ok(entityModel);
    }

    /**
     * Busca una notificacion asociada a un usuario.
     *
     * @param usuarioId identificador del usuario
     * @return la notificacion encontrada como recurso HATEOAS
     */
    @GetMapping("/usuario/{usuarioId}")
    @Operation(
            summary = "Busqueda de notificacion por usuario",
            description = "Se devuelve la notificacion asociada a un id de usuario como recurso HATEOAS"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<EntityModel<Notificacion>> findByUsuarioId(
            @Parameter(description = "Id del usuario", required = true, example = "1")
            @PathVariable Long usuarioId
    ) {
        EntityModel<Notificacion> entityModel = this.notificacionModelAssembler.toModel(
                this.notificacionService.findByUsuarioId(usuarioId)
        );
        return ResponseEntity.ok(entityModel);
    }

    /**
     * Busca una notificacion asociada a un equipo.
     *
     * @param equipoId identificador del equipo
     * @return la notificacion encontrada como recurso HATEOAS
     */
    @GetMapping("/equipo/{equipoId}")
    @Operation(
            summary = "Busqueda de notificacion por equipo",
            description = "Se devuelve la notificacion asociada a un id de equipo como recurso HATEOAS"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<EntityModel<Notificacion>> findByEquipoId(
            @Parameter(description = "Id del equipo", required = true, example = "1")
            @PathVariable Long equipoId
    ) {
        EntityModel<Notificacion> entityModel = this.notificacionModelAssembler.toModel(
                this.notificacionService.findByEquipoId(equipoId)
        );
        return ResponseEntity.ok(entityModel);
    }

    /**
     * Crea una nueva notificacion.
     *
     * @param notificacion datos de la notificacion a crear (validados)
     * @return la notificacion creada como recurso HATEOAS
     */
    @PostMapping
    @Operation(summary = "Guardado de notificacion", description = "Esta es la forma de guardar una notificacion")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Notificacion a crear", required = true,
            content = @Content(schema = @Schema(implementation = Notificacion.class))
    )
    @ApiResponse(responseCode = "201", description = "Notificacion creada")
    public ResponseEntity<EntityModel<Notificacion>> save(@Valid @RequestBody Notificacion notificacion) {
        Notificacion notificacionCreate = this.notificacionService.save(notificacion);
        EntityModel<Notificacion> entityModel = this.notificacionModelAssembler.toModel(notificacionCreate);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(entityModel);
    }

    /**
     * Actualiza una notificacion existente.
     *
     * @param notificacionId identificador de la notificacion a actualizar
     * @param notificacion   nuevos datos de la notificacion (validados)
     * @return la notificacion actualizada como recurso HATEOAS
     */
    @PutMapping("/{notificacionId}")
    @Operation(summary = "Actualizacion de notificacion", description = "Se actualizan los datos de una notificacion existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notificacion actualizada"),
            @ApiResponse(responseCode = "404", description = "Notificacion no se encuentra en la BD")
    })
    public ResponseEntity<EntityModel<Notificacion>> update(
            @Parameter(description = "Id de la notificacion a actualizar", required = true, example = "1")
            @PathVariable Long notificacionId,
            @Valid @RequestBody Notificacion notificacion
    ) {
        Notificacion notificacionUpdate = this.notificacionService.update(notificacionId, notificacion);
        EntityModel<Notificacion> entityModel = this.notificacionModelAssembler.toModel(notificacionUpdate);
        return ResponseEntity.ok(entityModel);
    }

    /**
     * Elimina una notificacion por su identificador.
     *
     * @param notificacionId identificador de la notificacion a eliminar
     * @return respuesta {@code 204 No Content}
     */
    @DeleteMapping("/{notificacionId}")
    @Operation(summary = "Eliminacion de notificacion", description = "Se elimina una notificacion por su id")
    @ApiResponse(responseCode = "204", description = "Notificacion eliminada")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Id de la notificacion a eliminar", required = true, example = "1")
            @PathVariable Long notificacionId
    ) {
        this.notificacionService.deleteBynotificacionId(notificacionId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
