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
    private Long resultadoId;

    @NotNull(message = "El ID de la partida no puede ser nulo")
    @Column(name = "partida_id", nullable = false)
    private Long partidaId;

    @NotNull(message = "El ID del equipo A no puede ser nulo")
    @Column(name = "team_a_id", nullable = false)
    private Long teamAId;

    @NotNull(message = "El ID del equipo B no puede ser nulo")
    @Column(name = "team_b_id", nullable = false)
    private Long teamBId;

    @Min(value = 0, message = "El puntaje A no puede ser negativo")
    @Column(name = "score_a")
    private Integer scoreA;

    @Min(value = 0, message = "El puntaje B no puede ser negativo")
    @Column(name = "score_b")
    private Integer scoreB;

    @Column(name = "winner_id")
    private Long winnerId;

    @Column(nullable = false)
    private String estado;

    @Embedded
    private Audit audit = new Audit();
}