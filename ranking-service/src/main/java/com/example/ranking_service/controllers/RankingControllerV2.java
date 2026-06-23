package com.example.ranking_service.controllers;

import com.example.ranking_service.assemblers.RankingModelAssembler;
import com.example.ranking_service.models.Ranking;
import com.example.ranking_service.services.RankingService;
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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

/**
 * Controlador REST versión 2 para la gestión de rankings.
 *
 * <p>Ofrece las operaciones CRUD y de consulta devolviendo representaciones
 * HATEOAS ({@link EntityModel} / {@link CollectionModel}) con enlaces de
 * navegación.</p>
 */
@RestController
@RequestMapping("/api/v2/rankings")
@Validated
@Tag(name = "Rankings V2", description = "Metodos CRUD HATEOAS para la gestión de rankings")
public class RankingControllerV2 {

    @Autowired
    private RankingService rankingService;

    @Autowired
    private RankingModelAssembler rankingModelAssembler;

    /**
     * Lista todos los rankings.
     *
     * @return colección HATEOAS de {@link Ranking} con un enlace {@code self}
     */
    @GetMapping
    @Operation(
            summary = "Listado de todos los rankings",
            description = "Se devuelve una colección HATEOAS con todos los rankings"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<CollectionModel<EntityModel<Ranking>>> findAll() {
        List<EntityModel<Ranking>> entityModels = this.rankingService.findAll()
                .stream()
                .map(rankingModelAssembler::toModel)
                .toList();
        CollectionModel<EntityModel<Ranking>> collectionModel = CollectionModel.of(
                entityModels,
                linkTo(methodOn(RankingControllerV2.class).findAll()).withSelfRel()
        );
        return ResponseEntity.status(HttpStatus.OK).body(collectionModel);
    }

    /**
     * Busca un ranking por su identificador.
     *
     * @param id identificador del ranking
     * @return el ranking encontrado como recurso HATEOAS
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Busqueda de un ranking por id",
            description = "Se devuelve un ranking, en caso contrario se devuelve una excepcion"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ranking encontrado"),
            @ApiResponse(responseCode = "404", description = "Ranking no se encuentra en la BD")
    })
    public ResponseEntity<EntityModel<Ranking>> findById(
            @Parameter(description = "Id del ranking a buscar", required = true, example = "1")
            @PathVariable Long id
    ) {
        EntityModel<Ranking> entityModel = this.rankingModelAssembler.toModel(
                this.rankingService.findById(id)
        );
        return ResponseEntity.status(HttpStatus.OK).body(entityModel);
    }

    /**
     * Crea un nuevo registro de ranking.
     *
     * @param ranking datos del ranking a crear (validados)
     * @return el ranking creado como recurso HATEOAS
     */
    @PostMapping
    @Operation(summary = "Guardado de ranking", description = "Esta es la forma de guardar un registro de ranking")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Ranking a crear", required = true,
            content = @Content(schema = @Schema(implementation = Ranking.class))
    )
    @ApiResponse(responseCode = "201", description = "Ranking creado")
    public ResponseEntity<EntityModel<Ranking>> save(@Valid @RequestBody Ranking ranking) {
        Ranking rankingCreado = this.rankingService.save(ranking);
        EntityModel<Ranking> entityModel = this.rankingModelAssembler.toModel(rankingCreado);
        return ResponseEntity.status(HttpStatus.CREATED).body(entityModel);
    }

    /**
     * Actualiza un ranking existente.
     *
     * @param id      identificador del ranking a actualizar
     * @param ranking nuevos datos del ranking (validados)
     * @return el ranking actualizado como recurso HATEOAS
     */
    @PutMapping("/{id}")
    @Operation(summary = "Actualizacion de ranking", description = "Se actualizan los datos de un ranking existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ranking actualizado"),
            @ApiResponse(responseCode = "404", description = "Ranking no se encuentra en la BD")
    })
    public ResponseEntity<EntityModel<Ranking>> updateById(
            @Parameter(description = "Id del ranking a actualizar", required = true, example = "1")
            @PathVariable Long id,
            @Valid @RequestBody Ranking ranking
    ) {
        Ranking rankingUpdate = this.rankingService.updateById(ranking, id);
        EntityModel<Ranking> entityModel = this.rankingModelAssembler.toModel(rankingUpdate);
        return ResponseEntity.status(HttpStatus.OK).body(entityModel);
    }

    /**
     * Elimina un ranking por su identificador.
     *
     * @param id identificador del ranking a eliminar
     * @return respuesta {@code 204 No Content}
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminacion de ranking", description = "Se elimina un ranking por su id")
    @ApiResponse(responseCode = "204", description = "Ranking eliminado")
    public ResponseEntity<Void> deleteById(
            @Parameter(description = "Id del ranking a eliminar", required = true, example = "1")
            @PathVariable Long id
    ) {
        this.rankingService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Lista los rankings asociados a un torneo.
     *
     * @param tourId identificador del torneo
     * @return colección HATEOAS de rankings del torneo
     */
    @GetMapping("/tournament/{tourId}")
    @Operation(
            summary = "Listado de rankings por torneo",
            description = "Se devuelven los rankings asociados a un torneo como colección HATEOAS"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<CollectionModel<EntityModel<Ranking>>> findByTourId(
            @Parameter(description = "Id del torneo", required = true, example = "1")
            @PathVariable Long tourId
    ) {
        List<EntityModel<Ranking>> entityModels = this.rankingService.findByTourId(tourId)
                .stream()
                .map(rankingModelAssembler::toModel)
                .toList();
        CollectionModel<EntityModel<Ranking>> collectionModel = CollectionModel.of(
                entityModels,
                linkTo(methodOn(RankingControllerV2.class).findByTourId(tourId)).withSelfRel()
        );
        return ResponseEntity.status(HttpStatus.OK).body(collectionModel);
    }

    /**
     * Busca la posicion de un participante dentro de un torneo.
     *
     * @param tourId         identificador del torneo
     * @param participanteId identificador del participante
     * @return el ranking encontrado como recurso HATEOAS
     */
    @GetMapping("/tournament/{tourId}/participante/{participanteId}")
    @Operation(
            summary = "Busqueda de ranking por torneo y participante",
            description = "Se devuelve la posicion de un participante en un torneo como recurso HATEOAS"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ranking encontrado"),
            @ApiResponse(responseCode = "404", description = "Ranking no se encuentra en la BD")
    })
    public ResponseEntity<EntityModel<Ranking>> findByTourIdAndParticipanteId(
            @Parameter(description = "Id del torneo", required = true, example = "1")
            @PathVariable Long tourId,
            @Parameter(description = "Id del participante", required = true, example = "1")
            @PathVariable Long participanteId
    ) {
        EntityModel<Ranking> entityModel = this.rankingModelAssembler.toModel(
                this.rankingService.findByTourIdAndParticipanteId(tourId, participanteId)
        );
        return ResponseEntity.status(HttpStatus.OK).body(entityModel);
    }

    /**
     * Actualiza los puntos, victorias y derrotas de un ranking.
     *
     * @param id        identificador del ranking
     * @param puntos    puntos a sumar
     * @param victorias victorias a sumar
     * @param derrotas  derrotas a sumar
     * @return el ranking actualizado como recurso HATEOAS
     */
    @PatchMapping("/{id}/puntos")
    @Operation(summary = "Actualizacion de puntos", description = "Actualiza puntos, victorias y derrotas de un ranking")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Puntos actualizados"),
            @ApiResponse(responseCode = "404", description = "Ranking no se encuentra en la BD")
    })
    public ResponseEntity<EntityModel<Ranking>> actualizarPuntos(
            @Parameter(description = "Id del ranking", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Puntos a sumar", required = true, example = "3")
            @RequestParam Integer puntos,
            @Parameter(description = "Victorias a sumar", required = true, example = "1")
            @RequestParam Integer victorias,
            @Parameter(description = "Derrotas a sumar", required = true, example = "0")
            @RequestParam Integer derrotas
    ) {
        EntityModel<Ranking> entityModel = this.rankingModelAssembler.toModel(
                this.rankingService.actualizarPuntos(id, puntos, victorias, derrotas)
        );
        return ResponseEntity.status(HttpStatus.OK).body(entityModel);
    }

    /**
     * Cierra el ranking de un torneo.
     *
     * @param tourId identificador del torneo
     * @return respuesta {@code 204 No Content}
     */
    @PatchMapping("/tournament/{tourId}/cerrar")
    @Operation(summary = "Cierre de ranking", description = "Cierra el ranking de un torneo al finalizar")
    @ApiResponse(responseCode = "204", description = "Ranking cerrado")
    public ResponseEntity<Void> cerrarRanking(
            @Parameter(description = "Id del torneo", required = true, example = "1")
            @PathVariable Long tourId
    ) {
        this.rankingService.cerrarRanking(tourId);
        return ResponseEntity.noContent().build();
    }
}
