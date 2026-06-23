package eSports_Arena_Manager.result_service.assemblers;

import eSports_Arena_Manager.result_service.controllers.ResultControllerV2;
import eSports_Arena_Manager.result_service.models.Result;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ResultModelAssembler implements RepresentationModelAssembler<Result, EntityModel<Result>> {

    @Override
    public EntityModel<Result> toModel(Result result) {
        return EntityModel.of(
                result,
                linkTo(methodOn(ResultControllerV2.class).findById(result.getResultadoId())).withSelfRel(),
                linkTo(methodOn(ResultControllerV2.class).findAll()).withRel("resultados"));
    }
}
