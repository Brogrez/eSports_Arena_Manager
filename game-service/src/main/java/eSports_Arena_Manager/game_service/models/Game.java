package eSports_Arena_Manager.game_service.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "games")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "game_id")
    private Long userId;

    @NotBlank(message = "El campo de nombre no puede ser vacio")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "El campo de genero no puede ser vacio")
    @Column(nullable = false)
    private String genero;

    @NotBlank(message = "El campo de modalidad no puede ser vacio")
    @Column(nullable = false)
    private String modalidad;

    @NotNull(message = "Los jugadores por equipo no pueden ser nulos")
    @Positive(message = "Los jugadores por equipo deben ser mayor a 0")
    private Integer jugadoresPorEquipo;

    @NotBlank(message = "El campo de estado no puede ser vacio")
    @Column(nullable = false)
    private String estado;

}
