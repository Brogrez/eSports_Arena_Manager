package com.duoc.team_service.assemblers;


import com.duoc.team_service.controllers.MiembroEquipoControllerV2;
import com.duoc.team_service.models.MiembroEquipo;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class MiembroEquipoModelAssembler implements RepresentationModelAssembler<MiembroEquipo, EntityModel<MiembroEquipo>> {

    @Override
    public EntityModel<MiembroEquipo> toModel(MiembroEquipo miembroEquipo) {
        return EntityModel.of(
                miembroEquipo,
                linkTo(methodOn(MiembroEquipoControllerV2.class).findByMEquipoId(miembroEquipo.getMEquipoId())).withSelfRel(),
                linkTo(methodOn(MiembroEquipoControllerV2.class).findAll()).withRel("miembros_equipo"));
    }
}
