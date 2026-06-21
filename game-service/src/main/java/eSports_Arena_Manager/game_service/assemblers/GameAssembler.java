package eSports_Arena_Manager.game_service.assemblers;

import eSports_Arena_Manager.game_service.controllers.GameControllerV2;
import eSports_Arena_Manager.game_service.models.Game;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class GameAssembler implements RepresentationModelAssembler<Game, EntityModel<Game>> {
    @Override
    public EntityModel<Game> toModel(Game game) {
        return EntityModel.of(game,
                linkTo(methodOn(GameControllerV2.class).findById(game.getGameId())).withSelfRel(),
                linkTo(methodOn(GameControllerV2.class).findAll()).withRel("games"));
    }
}
