package eSports_Arena_Manager.sanction_service.controllers;


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
import org.apache.coyote.Response;
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
@RequestMapping("/api/v1/sanctions")
@Validated
@Tag(name = "Sanctions V1", description = "Metodos CRUD para la gestión de sanciones")
public class SanctionController {

    @Autowired
    private SanctionService sanctionService;

    // findAll devuelve la lista de sanciones presentes en la base de datos.
    @GetMapping
    @Operation(
            summary = "Listado de todas las sanciones",
            description = "Se devuelve una lista con todas las sanciones registradas en la BD"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<List<Sanction>> findAll() {
        return ResponseEntity.ok(sanctionService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Busqueda de una sancion por id",
            description = "Se devuelve una sancion, en caso contrario se devuelve una excepcion"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Sancion encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Sanction.class))),
            @ApiResponse(responseCode = "404", description = "Sancion no se encuentra en la BD")
    })
    public ResponseEntity<Sanction> findById(
            @Parameter(description = "Id de la sancion a buscar", required = true, example = "1")
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(sanctionService.findById(id));
    }

    // POST /api/v1/sanctions => crear. @Valid valida el body; @RequestBody convierte el JSON.
    @PostMapping
    @Operation(summary = "Guardado de sancion", description = "Esta es la forma de guardar una sancion")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Sancion a crear", required = true,
            content = @Content(schema = @Schema(implementation = Sanction.class))
    )
    @ApiResponse(responseCode = "201", description = "Sancion creada")
    public ResponseEntity<Sanction> save(@Valid @RequestBody Sanction sanction) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(sanctionService.save(sanction));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizacion de sancion", description = "Se actualizan los datos de una sancion existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sancion actualizada"),
            @ApiResponse(responseCode = "404", description = "Sancion no se encuentra en la BD")
    })
    public ResponseEntity<Sanction> updateById(
            @Parameter(description = "Id de la sancion a actualizar", required = true, example = "1")
            @PathVariable Long id,
            @Valid @RequestBody Sanction sanction
    ) {
        return ResponseEntity.ok(sanctionService.updateById(id, sanction));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminacion de sancion", description = "Se elimina una sancion por su id")
    @ApiResponse(responseCode = "204", description = "Sancion eliminada")
    public ResponseEntity<Void> deleteById(
            @Parameter(description = "Id de la sancion a eliminar", required = true, example = "1")
            @PathVariable Long id
    ) {
        sanctionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    @Operation(
            summary = "Listado de sanciones por usuario",
            description = "Se devuelven las sanciones asociadas a un usuario especifico"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<List<Sanction>> findByUsuarioId(
            @Parameter(description = "Id del usuario", required = true, example = "1")
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(sanctionService.findByUsuarioId(userId));
    }

    @GetMapping("/team/{teamId}")
    @Operation(
            summary = "Listado de sanciones por equipo",
            description = "Se devuelven las sanciones asociadas a un equipo especifico"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<List<Sanction>> findByTeamId(
            @Parameter(description = "Id del equipo", required = true, example = "1")
            @PathVariable Long teamId
    ) {
        return ResponseEntity.ok(sanctionService.findByTeamId(teamId));
    }

    @GetMapping("/estado/{estado}")
    @Operation(
            summary = "Listado de sanciones por estado",
            description = "Se devuelven las sanciones filtradas por su estado actual"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<List<Sanction>> findByEstado(
            @Parameter(description = "Estado de la sancion a buscar", required = true, example = "ACTIVA")
            @PathVariable String estado
    ) {
        return ResponseEntity.ok(sanctionService.findByEstado(estado));
    }

    @PatchMapping("/{id}/cerrar")
    @Operation(
            summary = "Cierre de sancion",
            description = "Actualiza el estado de una sancion especifica marcandola como cerrada"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sancion cerrada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Sancion no se encuentra en la BD")
    })
    public ResponseEntity<Sanction> cerrar(
            @Parameter(description = "Id de la sancion a cerrar", required = true, example = "1")
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(sanctionService.cerrar(id));
    }
}