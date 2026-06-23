package com.duoc.prize_service.assemblers;

import com.duoc.prize_service.controllers.PrizeControllerV2;
import com.duoc.prize_service.models.PremioAsignado;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class PremioAsignadoModelAssembler implements RepresentationModelAssembler<PremioAsignado, EntityModel<PremioAsignado>> {

    @Override
    public EntityModel<PremioAsignado> toModel(PremioAsignado premioAsignado) {
        return EntityModel.of(
                premioAsignado,
                linkTo(methodOn(PrizeControllerV2.class).findById(premioAsignado.getPremioId())).withRel("premio"));
    }
}
