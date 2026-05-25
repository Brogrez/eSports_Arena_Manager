package com.duoc.prize_service.models; // Alineado a tu ruta física real actual

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

    @NotBlank(message = "El campo de nombre no puede ser vacio")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "El campo de descripcion no puede ser vacio")
    @Column(nullable = false)
    private String descripcion;

    @NotNull(message = "El campo de valor no puede ser nulo")
    @Column(nullable = false)
    private Double valor;

    @NotNull(message = "El ID del torneo no puede ser nulo")
    @Column(name = "torneo_id", nullable = false)
    private Long torneoId;


    @Column(nullable = false)
    private String estado;

    @Embedded
    private Audit audit = new Audit();
}