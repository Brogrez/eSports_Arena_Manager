package eSports_Arena_Manager.result_service.controllers;

import eSports_Arena_Manager.result_service.models.Result;
import eSports_Arena_Manager.result_service.services.ResultService;
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
@RequestMapping("/api/v1/results")
@Validated
@Tag(name = "Results V1", description = "Metodos CRUD para la gestión de resultados")
public class ResultController {

    @Autowired
    private ResultService resultService;

    // findAll devuelve la lista de resultados presentes en la base de datos.
    @GetMapping
    @Operation(
            summary = "Listado de todos los resultados",
            description = "Se devuelve una lista con todos los resultados registrados en la BD"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<List<Result>> findAll() {
        return ResponseEntity.ok(resultService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Busqueda de un resultado por id",
            description = "Se devuelve un resultado, en caso contrario se devuelve una excepcion"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Resultado encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Result.class))),
            @ApiResponse(responseCode = "404", description = "Resultado no se encuentra en la BD")
    })
    public ResponseEntity<Result> findById(
            @Parameter(description = "Id del resultado a buscar", required = true, example = "1")
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(resultService.findById(id));
    }

    @GetMapping("/partida/{partidaId}")
    @Operation(
            summary = "Busqueda de resultado por partida",
            description = "Se devuelve el resultado asociado a una partida especifica"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<Result> findByPartidaId(
            @Parameter(description = "Id de la partida", required = true, example = "1")
            @PathVariable Long partidaId
    ) {
        return ResponseEntity.ok(resultService.findByPartidaId(partidaId));
    }

    @GetMapping("/estado/{estado}")
    @Operation(
            summary = "Listado de resultados por estado",
            description = "Se devuelven los resultados filtrados por su estado actual"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<List<Result>> findByEstado(
            @Parameter(description = "Estado del resultado a buscar", required = true, example = "FINALIZADO")
            @PathVariable String estado
    ) {
        return ResponseEntity.ok(resultService.findByEstado(estado));
    }

    // POST /api/v1/results => crear. @Valid valida el body; @RequestBody convierte el JSON.
    @PostMapping
    @Operation(summary = "Guardado de resultado", description = "Esta es la forma de guardar un resultado")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Resultado a crear", required = true,
            content = @Content(schema = @Schema(implementation = Result.class))
    )
    @ApiResponse(responseCode = "201", description = "Resultado creado")
    public ResponseEntity<Result> save(@Valid @RequestBody Result result) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(resultService.save(result));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizacion de resultado", description = "Se actualizan los datos de un resultado existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resultado actualizado"),
            @ApiResponse(responseCode = "404", description = "Resultado no se encuentra en la BD")
    })
    public ResponseEntity<Result> updateById(
            @Parameter(description = "Id del resultado a actualizar", required = true, example = "1")
            @PathVariable Long id,
            @Valid @RequestBody Result result
    ) {
        return ResponseEntity.ok(resultService.updateById(id, result));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminacion de resultado", description = "Se elimina un resultado por su id")
    @ApiResponse(responseCode = "204", description = "Resultado eliminado")
    public ResponseEntity<Void> deleteById(
            @Parameter(description = "Id del resultado a eliminar", required = true, example = "1")
            @PathVariable Long id
    ) {
        resultService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}