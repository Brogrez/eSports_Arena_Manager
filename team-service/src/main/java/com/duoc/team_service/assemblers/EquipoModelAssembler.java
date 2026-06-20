package com.duoc.team_service.assemblers;

import com.duoc.team_service.controllers.EquipoController;
import com.duoc.team_service.controllers.EquipoControllerV2;
import com.duoc.team_service.models.Equipo;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Component
public class EquipoModelAssembler {

    @Override
    public EntityModel<Equipo> toModel(Equipo equipo) {
        return EntityModel.of(equipo,
                linkTo(methodOn(EquipoControllerV2.class).findById(equipo.getEquipoId())).withSelfRel(),
                linkTo(methodOn(EquipoControllerV2.class).findAll()).withRel("equipos"));
    }


}
