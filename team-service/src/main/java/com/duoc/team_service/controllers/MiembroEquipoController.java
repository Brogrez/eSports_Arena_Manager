package com.duoc.team_service.controllers;

import com.duoc.team_service.models.MiembroEquipo;
import com.duoc.team_service.models.dtos.MiembroEquipoDTO;
import com.duoc.team_service.services.MiembroEquipoService;
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


@RestController
@RequestMapping("/api/v1/miembros_equipo")
@Validated
@Tag(name = "Miembros Equipo V1", description = "Metodos CRUD para la gestión de miembros de equipo")
public class MiembroEquipoController {

    @Autowired
    private MiembroEquipoService miembroEquipoService;


    @GetMapping
    @Operation(
            summary = "Listado de todos los miembros de equipo",
            description = "Se devuelve una lista con los miembros enriquecidos en formato DTO"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<List<MiembroEquipoDTO>> findAll() {
        return ResponseEntity.ok(miembroEquipoService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Busqueda de un miembro de equipo por id",
            description = "Se devuelve un miembro, en caso contrario se devuelve una excepcion"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Miembro encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MiembroEquipo.class))),
            @ApiResponse(responseCode = "404", description = "Miembro no se encuentra en la BD")
    })
    public ResponseEntity<MiembroEquipo> getById(
            @Parameter(description = "Id del miembro a buscar", required = true, example = "1")
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(miembroEquipoService.findBymEquipoId(id));
    }

    // POST /api/v1/miembros_equipo => crear. @Valid valida el body; @RequestBody convierte el JSON.
    @PostMapping
    @Operation(summary = "Guardado de miembro de equipo", description = "Esta es la forma de guardar un miembro de equipo")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Miembro a crear", required = true,
            content = @Content(schema = @Schema(implementation = MiembroEquipo.class))
    )
    @ApiResponse(responseCode = "201", description = "Miembro creado")
    public ResponseEntity<MiembroEquipo> save(@Valid @RequestBody MiembroEquipo miembroEquipo) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(miembroEquipoService.save(miembroEquipo));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizacion de miembro de equipo", description = "Se actualizan los datos de un miembro existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Miembro actualizado"),
            @ApiResponse(responseCode = "404", description = "Miembro no se encuentra en la BD")
    })
    public ResponseEntity<MiembroEquipo> update(
            @Parameter(description = "Id del miembro a actualizar", required = true, example = "1")
            @PathVariable Long id,
            @Valid @RequestBody MiembroEquipo miembroEquipo
    ) {
        return ResponseEntity.ok(miembroEquipoService.update(id, miembroEquipo));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminacion de miembro de equipo", description = "Se elimina un miembro por su id")
    @ApiResponse(responseCode = "204", description = "Miembro eliminado")
    public ResponseEntity<Void> deleteById(
            @Parameter(description = "Id del miembro a eliminar", required = true, example = "1")
            @PathVariable Long id
    ) {
        miembroEquipoService.deleteByMiembroId(miembroEquipoService.findBymEquipoId(id));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/equipo/{mEquipoId}")
    @Operation(
            summary = "Busqueda de miembro por equipo",
            description = "Se devuelve el miembro de equipo asociado a un id de equipo"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<MiembroEquipo> findByMEquipoId(
            @Parameter(description = "Id del equipo", required = true, example = "1")
            @PathVariable Long mEquipoId
    ) {
        return ResponseEntity.ok(miembroEquipoService.findBymEquipoId(mEquipoId));
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(
            summary = "Listado de miembros por usuario",
            description = "Se devuelven los miembros de equipo asociados a un usuario"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<List<MiembroEquipo>> findByUsuarioId(
            @Parameter(description = "Id del usuario", required = true, example = "1")
            @PathVariable Long usuarioId
    ) {
        return ResponseEntity.ok(miembroEquipoService.findByUsuarioId(usuarioId));
    }

    @GetMapping("/rol/{rolDentroEquipo}")
    @Operation(
            summary = "Busqueda de miembro por rol",
            description = "Se devuelve el miembro de equipo buscando por su rol exacto"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<MiembroEquipo> findByRolDentroEquipo(
            @Parameter(description = "Rol dentro del equipo", required = true, example = "Líder")
            @PathVariable String rolDentroEquipo
    ) {
        return ResponseEntity.ok(miembroEquipoService.findByRolDentroEquipo(rolDentroEquipo));
    }

}

