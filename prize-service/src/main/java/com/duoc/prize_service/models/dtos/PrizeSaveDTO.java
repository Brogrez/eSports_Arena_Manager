package com.duoc.prize_service.models.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PrizeSaveDTO {

    @NotBlank(message = "El nombre del premio es obligatorio")
    private String nombre;

    @NotNull(message = "El monto no puede ser nulo")
    @Positive(message = "El monto debe ser un valor mayor a cero")
    private Double monto;

    @NotNull(message = "El ID del torneo es obligatorio")
    private Long torneoId; // ID plano para mantener el desacoplamiento estricto

    @NotBlank(message = "El estado del premio es obligatorio (ej: ASIGNADO, ENTREGADO)")
    private String estado;
}