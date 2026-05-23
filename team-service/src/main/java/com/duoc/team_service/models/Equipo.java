package com.duoc.team_service.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="equipos")
@ToString
@Getter
@Setter
@NoArgsConstructor
public class Equipo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="equipo_Id")
    private Long equipoId;

    @NotBlank(message = "tiene que haber un nombre en el equipo")
    @Column(nullable = false)
    private String nombreEquipo;

    @NotNull(message = "tiene que haber un capitan en el equipo")
    @Column(nullable = false)
    private Long capitanId;

    @NotNull(message = "debe tener el id del juego participante")
    @Column(name = "game_id", nullable = false)
    private Long JuegoPrincipalId;

    @NotBlank
    @Column(nullable = false)
    private String estado;



    @Embedded
    private Audit audit = new Audit();

}
