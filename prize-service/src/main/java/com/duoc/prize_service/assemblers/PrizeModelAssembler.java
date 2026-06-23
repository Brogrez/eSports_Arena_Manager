package com.duoc.prize_service.assemblers;

import com.duoc.prize_service.controllers.PrizeControllerV2;
import com.duoc.prize_service.models.Prize;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class PrizeModelAssembler implements RepresentationModelAssembler<Prize, EntityModel<Prize>> {

    @Override
    public EntityModel<Prize> toModel(Prize prize) {
        return EntityModel.of(
                prize,
                linkTo(methodOn(PrizeControllerV2.class).findById(prize.getPremioId())).withSelfRel(),
                linkTo(methodOn(PrizeControllerV2.class).findAll()).withRel("prize"));
    }
}
