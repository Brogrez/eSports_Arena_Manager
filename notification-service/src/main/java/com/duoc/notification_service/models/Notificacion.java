package com.duoc.notification_service.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="notificaciones")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificacionId;


    @NotNull(message = "el campo de usuarioID no puede estar vacio")
    @Column(nullable = false, unique = true)
    private Long usuarioId;

    @NotNull(message = "El campo de equipoID no puede estar vacio")
    @Column(nullable = false, unique = true)
    private Long equipoId;

    @NotBlank(message = "el campo de tipo no puede estar en blanco")
    @Column(nullable = false)
    private String tipo;

    @NotBlank(message = "el campo de mensaje no puede estar vacio")
    @Column(nullable = false)
    private String mensaje;

    @Column(nullable = false)
    private boolean leido = false;

    @Embedded
    private Audit audit = new Audit();



}
