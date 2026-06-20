package com.duoc.notification_service.assemblers;

import com.duoc.notification_service.controllers.NotificacionControllerV2;
import com.duoc.notification_service.models.Notificacion;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class NotificacionModelAssembler implements RepresentationModelAssembler<Notificacion, EntityModel<Notificacion>> {

    @Override
    public EntityModel<Notificacion> toModel(Notificacion notificacion) {
        return EntityModel.of(
                notificacion,
                linkTo(methodOn(NotificacionControllerV2.class).findByNotificacionId(notificacion.getNotificacionId())).withSelfRel(),
                linkTo(methodOn(NotificacionControllerV2.class).findAll()).withRel("notificaciones"));
    }
}
