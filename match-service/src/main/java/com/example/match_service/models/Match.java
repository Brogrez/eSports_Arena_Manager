package com.example.match_service.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "matchs")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "match_id")
    private Long matchId;

    @NotNull(message = "El torneo no puede ser nulo")
    @Column(nullable = false)
    private Long tourId;

    @NotNull(message = "El participante A no puede ser nulo")
    @Column(nullable = false)
    private Long participanteAId;

    @NotNull(message = "El participante B no puede ser nulo")
    @Column(nullable = false)
    private Long participanteBId;

    @NotBlank(message = "La ronda no puede estar vacía")
    @Column(nullable = false)
    private String round;

    @NotNull(message = "La fecha y hora no puede ser nula")
    @Column(nullable = false)
    private LocalDateTime fechaHora;

    @Column(nullable = false)
    private String estado;

    @Embedded
    Audit audit = new Audit();
}
