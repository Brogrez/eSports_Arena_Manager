package com.example.ranking_service.assemblers;

import com.example.ranking_service.controllers.RankingControllerV2;
import com.example.ranking_service.models.Ranking;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class RankingModelAssembler implements RepresentationModelAssembler<Ranking, EntityModel<Ranking>> {

    @Override
    public EntityModel<Ranking> toModel(Ranking ranking) {
        return EntityModel.of(
                ranking,
                linkTo(methodOn(RankingControllerV2.class).findById(ranking.getRankingId())).withSelfRel(),
                linkTo(methodOn(RankingControllerV2.class).findAll()).withRel("rankings"));
    }
}
