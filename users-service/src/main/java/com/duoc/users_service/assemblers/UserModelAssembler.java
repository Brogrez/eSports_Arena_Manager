package com.duoc.users_service.assemblers;


import com.duoc.users_service.controllers.UserControllerV2;
import com.duoc.users_service.models.User;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<User, EntityModel<User>> {

    @Override
    public EntityModel<User> toModel(User user) {

        return EntityModel.of(
                user,
                linkTo(methodOn(UserControllerV2.class).findById(user.getUserId())).withSelfRel(),
                linkTo(methodOn(UserControllerV2.class).findAll()).withRel("users")
        );

    }
}
