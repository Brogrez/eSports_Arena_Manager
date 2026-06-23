package eSports_Arena_Manager.sanction_service.assemblers;


import eSports_Arena_Manager.sanction_service.controllers.SanctionController;
import eSports_Arena_Manager.sanction_service.models.Sanction;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class SanctionModelAssembler implements RepresentationModelAssembler<Sanction, EntityModel<Sanction>> {

    @Override
    public EntityModel<Sanction> toModel(Sanction sanction) {
        return EntityModel.of(
                sanction,
                linkTo(methodOn(SanctionController.class).findById(sanction.getSancionId())).withSelfRel(),
                linkTo(methodOn(SanctionController.class).findAll()).withRel("sanctions"));
    }
}
