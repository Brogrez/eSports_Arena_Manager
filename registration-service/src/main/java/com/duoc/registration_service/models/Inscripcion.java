package com.duoc.registration_service.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Entity
@Table(name = "inscripciones")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Inscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="inscripcion_id")
    private Long inscripcionId;

    @NotNull(message = "El campo de id del torneo no puede ser vacio")
    @Column(nullable = false)
    private Long torneoId;

    @NotNull(message = "El campo de id del equipo no puede ser vacio")
    @Column(nullable = false)
    private Long equipoId;

    @NotNull(message = "el campo id de jugador no puede estar vacio")
    @Column(nullable = false)
    private Long jugadorId;

    @NotBlank(message = "El campo de tipo participante no puede estar en blanco")
    @Column(nullable = false)
    private String tipoParticipante;

    @Column(nullable = false)
    private String estado;

    @NotNull(message = "El campo de fecha de inscripcion no puede estar en blanco")
    @Column(nullable = false)
    private Date fechaInscripcion;

    @Embedded
    private Audit audit = new Audit();
}
