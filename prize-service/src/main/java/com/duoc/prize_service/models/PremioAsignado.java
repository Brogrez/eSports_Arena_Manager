package com.duoc.prize_service.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Table(name = "premios_asignados")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class PremioAsignado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "asignado_id")
    private Long asignadoId;

    @NotNull(message = "El premio no puede ser nulo")
    @Column(nullable = false)
    private Long premioId;

    @NotNull(message = "El participante no puede ser nulo")
    @Column(nullable = false)
    private Long participanteId;

    @Column(nullable = false)
    private LocalDate fechaAsignacion;

    @Embedded
    Audit audit = new Audit();
}
