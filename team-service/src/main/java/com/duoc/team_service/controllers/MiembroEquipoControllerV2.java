package com.duoc.team_service.controllers;

import com.duoc.team_service.assemblers.MiembroEquipoModelAssembler;
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
@RequestMapping("/api/v2/miembros_equipo")
@Validated
@Tag(name = "Miembros Equipo V2", description = "Metodos CRUD HATEOAS para la gestión de miembros de equipo")
public class MiembroEquipoControllerV2 {

    @Autowired
    private MiembroEquipoService miembroEquipoService;


    @Autowired
    private MiembroEquipoModelAssembler miembroEquipoModelAssembler;


    @GetMapping
    @Operation(
            summary = "Listado de todos los miembros de equipo",
            description = "Se devuelve una colección HATEOAS con los miembros enriquecidos"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<CollectionModel<MiembroEquipoDTO>> findAll() {

        List<MiembroEquipoDTO> miembros = this.miembroEquipoService.findAll();
        CollectionModel<MiembroEquipoDTO> collectionModel = CollectionModel.of(
                miembros,
                linkTo(methodOn(MiembroEquipoControllerV2.class).findAll()).withSelfRel()
        );
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(collectionModel);
    }


    @GetMapping("/{id}")
    @Operation(
            summary = "Busqueda de un miembro de equipo por id",
            description = "Se devuelve un miembro, en caso contrario se devuelve una excepcion"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Miembro encontrado"),
            @ApiResponse(responseCode = "404", description = "Miembro no se encuentra en la BD")
    })
    public ResponseEntity<EntityModel<MiembroEquipo>> findById(
            @Parameter(description = "Id del miembro a buscar", required = true, example = "1")
            @PathVariable Long id
    ) {
        EntityModel<MiembroEquipo> entityModel = this.miembroEquipoModelAssembler.toModel(
                this.miembroEquipoService.findBymEquipoId(id)
        );
        return ResponseEntity.ok(entityModel);
    }


    @PostMapping
    @Operation(summary = "Guardado de miembro de equipo", description = "Esta es la forma de guardar un miembro de equipo")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Miembro a crear", required = true,
            content = @Content(schema = @Schema(implementation = MiembroEquipo.class))
    )
    @ApiResponse(responseCode = "201", description = "Miembro creado")
    public ResponseEntity<EntityModel<MiembroEquipo>> save(@Valid @RequestBody MiembroEquipo miembroEquipo) {
        MiembroEquipo miembroCreate = this.miembroEquipoService.save(miembroEquipo);
        EntityModel<MiembroEquipo> entityModel = this.miembroEquipoModelAssembler.toModel(miembroCreate);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(entityModel);
    }


    @PutMapping("/{id}")
    @Operation(summary = "Actualizacion de miembro de equipo", description = "Se actualizan los datos de un miembro existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Miembro actualizado"),
            @ApiResponse(responseCode = "404", description = "Miembro no se encuentra en la BD")
    })
    public ResponseEntity<EntityModel<MiembroEquipo>> update(
            @Parameter(description = "Id del miembro a actualizar", required = true, example = "1")
            @PathVariable Long id,
            @Valid @RequestBody MiembroEquipo miembroEquipo
    ) {
        MiembroEquipo miembroUpdate = this.miembroEquipoService.update(id, miembroEquipo);
        EntityModel<MiembroEquipo> entityModel = this.miembroEquipoModelAssembler.toModel(miembroUpdate);
        return ResponseEntity.ok(entityModel);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminacion de miembro de equipo", description = "Se elimina un miembro por su id")
    @ApiResponse(responseCode = "204", description = "Miembro eliminado")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Id del miembro a eliminar", required = true, example = "1")
            @PathVariable Long id
    ) {
        this.miembroEquipoService.deleteByMiembroId(this.miembroEquipoService.findBymEquipoId(id));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @GetMapping("/equipo/{mEquipoId}")
    @Operation(
            summary = "Busqueda de miembro por equipo",
            description = "Se devuelve el miembro de equipo asociado a un id de equipo como recurso HATEOAS"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<EntityModel<MiembroEquipo>> findByMEquipoId(
            @Parameter(description = "Id del equipo", required = true, example = "1")
            @PathVariable Long mEquipoId
    ) {
        EntityModel<MiembroEquipo> entityModel = this.miembroEquipoModelAssembler.toModel(
                this.miembroEquipoService.findBymEquipoId(mEquipoId)
        );
        return ResponseEntity.ok(entityModel);
    }


    @GetMapping("/usuario/{usuarioId}")
    @Operation(
            summary = "Listado de miembros por usuario",
            description = "Se devuelven los miembros de equipo asociados a un usuario como colección HATEOAS"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<CollectionModel<EntityModel<MiembroEquipo>>> findByUsuarioId(
            @Parameter(description = "Id del usuario", required = true, example = "1")
            @PathVariable Long usuarioId
    ) {
        List<EntityModel<MiembroEquipo>> entityModels = this.miembroEquipoService.findByUsuarioId(usuarioId)
                .stream()
                .map(miembroEquipoModelAssembler::toModel)
                .toList();
        CollectionModel<EntityModel<MiembroEquipo>> collectionModel = CollectionModel.of(
                entityModels,
                linkTo(methodOn(MiembroEquipoControllerV2.class).findByUsuarioId(usuarioId)).withSelfRel()
        );
        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/rol/{rolDentroEquipo}")
    @Operation(
            summary = "Busqueda de miembro por rol",
            description = "Se devuelve el miembro de equipo buscando por su rol exacto como recurso HATEOAS"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<EntityModel<MiembroEquipo>> findByRolDentroEquipo(
            @Parameter(description = "Rol dentro del equipo", required = true, example = "Líder")
            @PathVariable String rolDentroEquipo
    ) {
        EntityModel<MiembroEquipo> entityModel = this.miembroEquipoModelAssembler.toModel(
                this.miembroEquipoService.findByRolDentroEquipo(rolDentroEquipo)
        );
        return ResponseEntity.ok(entityModel);
    }
}
