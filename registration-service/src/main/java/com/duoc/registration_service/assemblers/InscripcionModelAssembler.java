package com.duoc.registration_service.assemblers;

import com.duoc.registration_service.controllers.InscripcionControllerV2;
import com.duoc.registration_service.models.Inscripcion;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class InscripcionModelAssembler implements RepresentationModelAssembler<Inscripcion, EntityModel<Inscripcion>> {

    @Override
    public EntityModel<Inscripcion> toModel(Inscripcion inscripcion) {
        return EntityModel.of(
                inscripcion,
                linkTo(methodOn(InscripcionControllerV2.class).findById(inscripcion.getInscripcionId())).withSelfRel(),
                linkTo(methodOn(InscripcionControllerV2.class).findAll()).withRel("inscripciones"));
    }
}
