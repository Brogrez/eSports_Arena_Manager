package com.example.match_service.assemblers;

import com.example.match_service.controllers.MatchControllerV2;
import com.example.match_service.models.Match;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class MatchModelAssembler implements RepresentationModelAssembler<Match, EntityModel<Match>> {

    @Override
    public EntityModel<Match> toModel(Match match) {
        return EntityModel.of(
                match,
                linkTo(methodOn(MatchControllerV2.class).findById(match.getMatchId())).withSelfRel(),
                linkTo(methodOn(MatchControllerV2.class).findAll()).withRel("matchs"));
    }
}
