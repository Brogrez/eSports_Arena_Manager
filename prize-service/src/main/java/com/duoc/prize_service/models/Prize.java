package com.duoc.prize_service.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    private Long id;

    @NotBlank(message = "El nombre del premio no puede estar vacio")
    @Column(nullable = false)
    private String nombre;

    @NotNull(message = "El monto o valor del premio es obligatorio")
    @Column(nullable = false)
    private Double monto;

    // DESACOPLAMIENTO: ID de referencia al microservicio externo de torneos
    @NotNull(message = "El ID del torneo no puede ser nulo")
    @Column(name = "torneo_id", nullable = false)
    private Long torneoId;

    @Column(nullable = false)
    private String estado; // Ejemplo: "ASIGNADO", "ENTREGADO"

    @Embedded
    private Audit audit = new Audit();
}