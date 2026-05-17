package com.duoc.tournament_service.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Table(name = "tournaments")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Tour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "torneo_id")
    private Long tourId;

    @NotBlank(message = "El nombre del torneo no puede estar vacío")
    @Column(nullable = false)
    private String name;

    @NotNull(message = "El juego no puede ser nulo")
    @Column(name = "juego_id", nullable = false)
    private Long gameId;

    @NotNull(message = "La fecha de inicio no puede ser nula")
    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha de fin no puede ser nula")
    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @NotNull(message = "El cupo máximo no puede ser nulo")
    @Positive(message = "El cupo máximo debe ser mayor a 0")
    @Column(name = "cupo_maximo", nullable = false)
    private Integer cupoMaximo;

    @NotBlank(message = "La modalidad no puede estar vacía")
    @Column(nullable = false)
    private String modalidad;

    @Column(nullable = false)
    private String estado;

    @Embedded
    Audit audit = new Audit();
}
