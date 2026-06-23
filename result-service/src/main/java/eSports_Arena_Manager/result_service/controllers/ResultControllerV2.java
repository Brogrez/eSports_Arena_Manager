package eSports_Arena_Manager.result_service.controllers;

import eSports_Arena_Manager.result_service.assemblers.ResultModelAssembler;
import eSports_Arena_Manager.result_service.models.Result;
import eSports_Arena_Manager.result_service.services.ResultService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
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
@RequestMapping("/api/v2/results")
@Validated
@Tag(name = "Results V2", description = "Metodos CRUD HATEOAS para la gestión de resultados")
public class ResultControllerV2 {

    @Autowired
    private ResultService resultService;

    @Autowired
    private ResultModelAssembler resultModelAssembler;


    @GetMapping
    @Operation(
            summary = "Listado de todos los resultados",
            description = "Se devuelve una colección HATEOAS con todos los resultados registrados en la BD"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<CollectionModel<EntityModel<Result>>> findAll() {
        List<EntityModel<Result>> entityModels = this.resultService.findAll()
                .stream()
                .map(resultModelAssembler::toModel)
                .toList();
        CollectionModel<EntityModel<Result>> collectionModel = CollectionModel.of(
                entityModels,
                linkTo(methodOn(ResultControllerV2.class).findAll()).withSelfRel()
        );
        return ResponseEntity.ok(collectionModel);
    }


    @GetMapping("/{id}")
    @Operation(
            summary = "Busqueda de un resultado por id",
            description = "Se devuelve un resultado, en caso contrario se devuelve una excepcion"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resultado encontrado"),
            @ApiResponse(responseCode = "404", description = "Resultado no se encuentra en la BD")
    })
    public ResponseEntity<EntityModel<Result>> findById(
            @Parameter(description = "Id del resultado a buscar", required = true, example = "1")
            @PathVariable Long id
    ) {
        EntityModel<Result> entityModel = this.resultModelAssembler.toModel(
                this.resultService.findById(id)
        );
        return ResponseEntity.ok(entityModel);
    }

    @GetMapping("/partida/{partidaId}")
    @Operation(
            summary = "Busqueda de resultado por partida",
            description = "Se devuelve el resultado asociado a una partida especifica como recurso HATEOAS"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<EntityModel<Result>> findByPartidaId(
            @Parameter(description = "Id de la partida", required = true, example = "1")
            @PathVariable Long partidaId
    ) {
        EntityModel<Result> entityModel = this.resultModelAssembler.toModel(
                this.resultService.findByPartidaId(partidaId)
        );
        return ResponseEntity.ok(entityModel);
    }


    @GetMapping("/estado/{estado}")
    @Operation(
            summary = "Listado de resultados por estado",
            description = "Se devuelven los resultados filtrados por estado como colección HATEOAS"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<CollectionModel<EntityModel<Result>>> findByEstado(
            @Parameter(description = "Estado del resultado a buscar", required = true, example = "FINALIZADO")
            @PathVariable String estado
    ) {
        List<EntityModel<Result>> entityModels = this.resultService.findByEstado(estado)
                .stream()
                .map(resultModelAssembler::toModel)
                .toList();
        CollectionModel<EntityModel<Result>> collectionModel = CollectionModel.of(
                entityModels,
                linkTo(methodOn(ResultControllerV2.class).findByEstado(estado)).withSelfRel()
        );
        return ResponseEntity.ok(collectionModel);
    }


    @PostMapping
    @Operation(summary = "Guardado de resultado", description = "Esta es la forma de guardar un resultado")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Resultado a crear", required = true,
            content = @Content(schema = @Schema(implementation = Result.class))
    )
    @ApiResponse(responseCode = "201", description = "Resultado creado")
    public ResponseEntity<EntityModel<Result>> save(@Valid @RequestBody Result result) {
        Result resultCreate = this.resultService.save(result);
        EntityModel<Result> entityModel = this.resultModelAssembler.toModel(resultCreate);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(entityModel);
    }


    @PutMapping("/{id}")
    @Operation(summary = "Actualizacion de resultado", description = "Se actualizan los datos de un resultado existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resultado actualizado"),
            @ApiResponse(responseCode = "404", description = "Resultado no se encuentra en la BD")
    })
    public ResponseEntity<EntityModel<Result>> updateById(
            @Parameter(description = "Id del resultado a actualizar", required = true, example = "1")
            @PathVariable Long id,
            @Valid @RequestBody Result result
    ) {
        Result resultUpdate = this.resultService.updateById(id, result);
        EntityModel<Result> entityModel = this.resultModelAssembler.toModel(resultUpdate);
        return ResponseEntity.ok(entityModel);
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminacion de resultado", description = "Se elimina un resultado por su id")
    @ApiResponse(responseCode = "204", description = "Resultado eliminado")
    public ResponseEntity<Void> deleteById(
            @Parameter(description = "Id del resultado a eliminar", required = true, example = "1")
            @PathVariable Long id
    ) {
        this.resultService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}