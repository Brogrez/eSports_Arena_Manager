package com.duoc.prize_service.controllers;

import com.duoc.prize_service.assemblers.PremioAsignadoModelAssembler;
import com.duoc.prize_service.assemblers.PrizeModelAssembler;
import com.duoc.prize_service.models.PremioAsignado;
import com.duoc.prize_service.models.Prize;
import com.duoc.prize_service.services.PrizeService;
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
 * Controlador REST versión 2 para la gestión de premios.
 *
 * <p>Ofrece las operaciones CRUD, de consulta y de asignación de premios
 * devolviendo representaciones HATEOAS ({@link EntityModel} / {@link CollectionModel})
 * con enlaces de navegación.</p>
 */
@RestController
@RequestMapping("/api/v2/prizes")
@Validated
@Tag(name = "Prizes V2", description = "Metodos CRUD HATEOAS para la gestión de premios")
public class PrizeControllerV2 {

    @Autowired
    private PrizeService prizeService;

    @Autowired
    private PrizeModelAssembler prizeModelAssembler;

    @Autowired
    private PremioAsignadoModelAssembler premioAsignadoModelAssembler;

    /**
     * Lista todos los premios.
     *
     * @return colección HATEOAS de {@link Prize} con un enlace {@code self}
     */
    @GetMapping
    @Operation(
            summary = "Listado de todos los premios",
            description = "Se devuelve una colección HATEOAS con todos los premios"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<CollectionModel<EntityModel<Prize>>> findAll() {
        List<EntityModel<Prize>> entityModels = this.prizeService.findAll()
                .stream()
                .map(prizeModelAssembler::toModel)
                .toList();
        CollectionModel<EntityModel<Prize>> collectionModel = CollectionModel.of(
                entityModels,
                linkTo(methodOn(PrizeControllerV2.class).findAll()).withSelfRel()
        );
        return ResponseEntity.status(HttpStatus.OK).body(collectionModel);
    }

    /**
     * Busca un premio por su identificador.
     *
     * @param id identificador del premio
     * @return el premio encontrado como recurso HATEOAS
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Busqueda de un premio por id",
            description = "Se devuelve un premio, en caso contrario se devuelve una excepcion"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Premio encontrado"),
            @ApiResponse(responseCode = "404", description = "Premio no se encuentra en la BD")
    })
    public ResponseEntity<EntityModel<Prize>> findById(
            @Parameter(description = "Id del premio a buscar", required = true, example = "1")
            @PathVariable Long id
    ) {
        EntityModel<Prize> entityModel = this.prizeModelAssembler.toModel(
                this.prizeService.findById(id)
        );
        return ResponseEntity.status(HttpStatus.OK).body(entityModel);
    }

    /**
     * Crea un nuevo premio.
     *
     * @param premio datos del premio a crear (validados)
     * @return el premio creado como recurso HATEOAS
     */
    @PostMapping
    @Operation(summary = "Guardado de premio", description = "Esta es la forma de guardar un premio")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Premio a crear", required = true,
            content = @Content(schema = @Schema(implementation = Prize.class))
    )
    @ApiResponse(responseCode = "201", description = "Premio creado")
    public ResponseEntity<EntityModel<Prize>> save(@Valid @RequestBody Prize premio) {
        Prize prizeCreado = this.prizeService.save(premio);
        EntityModel<Prize> entityModel = this.prizeModelAssembler.toModel(prizeCreado);
        return ResponseEntity.status(HttpStatus.CREATED).body(entityModel);
    }

    /**
     * Actualiza un premio existente.
     *
     * @param id    identificador del premio a actualizar
     * @param prize nuevos datos del premio (validados)
     * @return el premio actualizado como recurso HATEOAS
     */
    @PutMapping("/{id}")
    @Operation(summary = "Actualizacion de premio", description = "Se actualizan los datos de un premio existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Premio actualizado"),
            @ApiResponse(responseCode = "404", description = "Premio no se encuentra en la BD")
    })
    public ResponseEntity<EntityModel<Prize>> updateById(
            @Parameter(description = "Id del premio a actualizar", required = true, example = "1")
            @PathVariable Long id,
            @Valid @RequestBody Prize prize
    ) {
        Prize prizeUpdate = this.prizeService.updateById(id, prize);
        EntityModel<Prize> entityModel = this.prizeModelAssembler.toModel(prizeUpdate);
        return ResponseEntity.status(HttpStatus.OK).body(entityModel);
    }

    /**
     * Elimina un premio por su identificador.
     *
     * @param id identificador del premio a eliminar
     * @return respuesta {@code 204 No Content}
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminacion de premio", description = "Se elimina un premio por su id")
    @ApiResponse(responseCode = "204", description = "Premio eliminado")
    public ResponseEntity<Void> deleteById(
            @Parameter(description = "Id del premio a eliminar", required = true, example = "1")
            @PathVariable Long id
    ) {
        this.prizeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Lista los premios asociados a un torneo.
     *
     * @param torneoId identificador del torneo
     * @return colección HATEOAS de premios del torneo
     */
    @GetMapping("/torneo/{torneoId}")
    @Operation(
            summary = "Listado de premios por torneo",
            description = "Se devuelven los premios asociados a un torneo como colección HATEOAS"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<CollectionModel<EntityModel<Prize>>> findByTorneoId(
            @Parameter(description = "Id del torneo", required = true, example = "1")
            @PathVariable Long torneoId
    ) {
        List<EntityModel<Prize>> entityModels = this.prizeService.findByTorneoId(torneoId)
                .stream()
                .map(prizeModelAssembler::toModel)
                .toList();
        CollectionModel<EntityModel<Prize>> collectionModel = CollectionModel.of(
                entityModels,
                linkTo(methodOn(PrizeControllerV2.class).findByTorneoId(torneoId)).withSelfRel()
        );
        return ResponseEntity.status(HttpStatus.OK).body(collectionModel);
    }

    /**
     * Lista los premios asociados a una posicion.
     *
     * @param posicion posicion del premio
     * @return colección HATEOAS de premios de esa posicion
     */
    @GetMapping("/posicion/{posicion}")
    @Operation(
            summary = "Busqueda de premios por posicion",
            description = "Se devuelven los premios segun su posicion como colección HATEOAS"
    )
    @ApiResponse(responseCode = "200", description = "Operacion Exitosa")
    public ResponseEntity<CollectionModel<EntityModel<Prize>>> findByPosicion(
            @Parameter(description = "Posicion del premio", required = true, example = "1")
            @PathVariable Integer posicion
    ) {
        List<EntityModel<Prize>> entityModels = this.prizeService.findByPosicion(posicion)
                .stream()
                .map(prizeModelAssembler::toModel)
                .toList();
        CollectionModel<EntityModel<Prize>> collectionModel = CollectionModel.of(
                entityModels,
                linkTo(methodOn(PrizeControllerV2.class).findByPosicion(posicion)).withSelfRel()
        );
        return ResponseEntity.status(HttpStatus.OK).body(collectionModel);
    }

    /**
     * Asigna un premio a un participante.
     *
     * @param premioId       identificador del premio
     * @param participanteId identificador del participante
     * @return la asignacion creada como recurso HATEOAS
     */
    @PostMapping("/{premioId}/asignar/{participanteId}")
    @Operation(summary = "Asignacion de premio", description = "Asigna un premio a un participante del torneo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Premio asignado"),
            @ApiResponse(responseCode = "404", description = "Premio o participante no encontrado")
    })
    public ResponseEntity<EntityModel<PremioAsignado>> asignarPremio(
            @Parameter(description = "Id del premio a asignar", required = true, example = "1")
            @PathVariable Long premioId,
            @Parameter(description = "Id del participante", required = true, example = "1")
            @PathVariable Long participanteId
    ) {
        PremioAsignado premioAsignado = this.prizeService.asignarPremio(premioId, participanteId);
        EntityModel<PremioAsignado> entityModel = this.premioAsignadoModelAssembler.toModel(premioAsignado);
        return ResponseEntity.status(HttpStatus.CREATED).body(entityModel);
    }
}
