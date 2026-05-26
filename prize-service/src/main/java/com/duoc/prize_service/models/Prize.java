package com.duoc.prize_service.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "premios")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Prize {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "premio_id")
    private Long premioId;

    @NotNull(message = "El ID del torneo no puede ser nulo")
    @Column(name = "torneo_id", nullable = false)
    private Long torneoId;

    @NotNull(message = "La posicion no puede ser nula")
    @Positive(message = "La posicion debe ser mayor a 0")
    @Column(nullable = false)
    private Integer posicion;

    @NotBlank(message = "La descripcion no puede estar vacía")
    @Column(nullable = false)
    private String descripcion;

    @NotNull(message = "El valor no puede ser nulo")
    @Column(nullable = false)
    private Double valor;

    @Column(nullable = false)
    private String estado;

    @Embedded
    private Audit audit = new Audit();
}