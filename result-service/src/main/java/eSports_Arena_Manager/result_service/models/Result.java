package eSports_Arena_Manager.result_service.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "resultados")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resultado_id")
    private Long id;

    @NotNull(message = "El ID del partido no puede ser nulo")
    private Long matchId;

    @NotNull(message = "El ID del equipo A no puede ser nulo")
    private Long teamAId;

    @NotNull(message = "El ID del equipo B no puede ser nulo")
    private Long teamBId;

    @Min(value = 0, message = "El puntaje del equipo A no puede ser negativo")
    private Integer scoreA;

    @Min(value = 0, message = "El puntaje del equipo B no puede ser negativo")
    private Integer scoreB;

    private Long winnerId;

    @NotBlank(message = "El estado del resultado no puede estar vacío")
    private String estado;

    @Embedded
    private Audit audit = new Audit();
}