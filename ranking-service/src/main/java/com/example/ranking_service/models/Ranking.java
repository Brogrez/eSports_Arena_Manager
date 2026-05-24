package com.example.ranking_service.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "rankings")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Ranking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rankign_id")
    private Long rankingId;

    @NotNull(message = "El torneo no puede ser nulo")
    @Column(nullable = false)
    private Long tourId;

    @NotNull(message = "El participante no puede ser nulo")
    @Column(nullable = false)
    private Long participanteId;

    @Column(nullable = false)
    private Integer puntos;

    @Column(nullable = false)
    private Integer victorias;

    @Column(nullable = false)
    private Integer derrotas;

    @Column(nullable = false)
    private Integer diferencia;

    @Column(nullable = false)
    private Integer posicion;

    @Embedded
    Audit audit = new Audit();
}
