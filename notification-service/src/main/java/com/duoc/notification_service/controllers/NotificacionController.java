package com.duoc.notification_service.controllers;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// @RestController: cada metodo devuelve datos (JSON), no vistas HTML.
// @RequestMapping: prefijo comun de las rutas (version 1 de la API).
// @Validated: activa la validacion de los @Valid. @Tag: agrupa los endpoints en Swagger.
@RestController
@RequestMapping("/api/v1/notificaciones")
@Validated
@Tag(name = "Notificaciones V1", description = "Metodos CRUD para la gestión de notificaciones")
public class NotificacionController {

    @Autowired
    private NotificacionService notificacionService;

    // findAll devuelve la lista de notificaciones.
    @GetMapping
    @Operation(
            summary = "Listado de todas las notificaciones",
            description = "Se devuelve una lista con todas las notificaciones registradas"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<List<Notificacion>> findAll() {
        return ResponseEntity.ok(notificacionService.findAll());
    }

    @GetMapping("/{notificacionId}")
    @Operation(
            summary = "Busqueda de una notificacion por id",
            description = "Se devuelve una notificacion, en caso contrario se devuelve una excepcion"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Notificacion encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Notificacion.class))),
            @ApiResponse(responseCode = "404", description = "Notificacion no se encuentra en la BD")
    })
    public ResponseEntity<Notificacion> findByNotificacionId(
            @Parameter(description = "Id de la notificacion a buscar", required = true, example = "1")
            @PathVariable Long notificacionId
    ) {
        return ResponseEntity.ok(notificacionService.findBynotificacionId(notificacionId));
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(
            summary = "Busqueda de notificacion por usuario",
            description = "Se devuelve la notificacion asociada a un id de usuario"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<Notificacion> findByUsuarioId(
            @Parameter(description = "Id del usuario", required = true, example = "1")
            @PathVariable Long usuarioId
    ) {
        return ResponseEntity.ok(notificacionService.findByUsuarioId(usuarioId));
    }

    @GetMapping("/equipo/{equipoId}")
    @Operation(
            summary = "Busqueda de notificacion por equipo",
            description = "Se devuelve la notificacion asociada a un id de equipo"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<Notificacion> findByEquipoId(
            @Parameter(description = "Id del equipo", required = true, example = "1")
            @PathVariable Long equipoId
    ) {
        return ResponseEntity.ok(notificacionService.findByEquipoId(equipoId));
    }


    @PostMapping
    @Operation(summary = "Guardado de notificacion", description = "Esta es la forma de guardar una notificacion")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Notificacion a crear", required = true,
            content = @Content(schema = @Schema(implementation = Notificacion.class))
    )
    @ApiResponse(responseCode = "201", description = "Notificacion creada")
    public ResponseEntity<Notificacion> save(@Valid @RequestBody Notificacion notificacion) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(notificacionService.save(notificacion));
    }

    @PutMapping("/{notificacionId}")
    @Operation(summary = "Actualizacion de notificacion", description = "Se actualizan los datos de una notificacion existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notificacion actualizada"),
            @ApiResponse(responseCode = "404", description = "Notificacion no se encuentra en la BD")
    })
    public ResponseEntity<Notificacion> update(
            @Parameter(description = "Id de la notificacion a actualizar", required = true, example = "1")
            @PathVariable Long notificacionId,
            @Valid @RequestBody Notificacion notificacion
    ) {
        return ResponseEntity.ok(notificacionService.update(notificacionId, notificacion));
    }

    @DeleteMapping("/{notificacionId}")
    @Operation(summary = "Eliminacion de notificacion", description = "Se elimina una notificacion por su id")
    @ApiResponse(responseCode = "204", description = "Notificacion eliminada")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Id de la notificacion a eliminar", required = true, example = "1")
            @PathVariable Long notificacionId
    ) {
        notificacionService.deleteBynotificacionId(notificacionId);
        return ResponseEntity.noContent().build();
    }
}
