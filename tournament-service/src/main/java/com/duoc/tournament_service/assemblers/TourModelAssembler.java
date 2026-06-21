package com.duoc.tournament_service.assemblers;

import com.duoc.tournament_service.controllers.TourControllerV2;
import com.duoc.tournament_service.models.Tour;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TourModelAssembler implements RepresentationModelAssembler<Tour, EntityModel<Tour>> {

    @Override
    public EntityModel<Tour> toModel(Tour tour) {
        return EntityModel.of(
                tour,
                linkTo(methodOn(TourControllerV2.class).findById(tour.getTourId())).withSelfRel(),
                linkTo(methodOn(TourControllerV2.class).findAll()).withRel("tournament"));
    }
}
