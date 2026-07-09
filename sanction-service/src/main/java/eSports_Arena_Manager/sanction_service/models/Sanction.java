package eSports_Arena_Manager.sanction_service.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "sanciones")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Sanction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sancion_id")
    private Long sancionId;

    @NotNull(message = "El campo de ID de usuario no puede ser vacio")
    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @NotNull(message = "El campo de ID de equipo no puede ser vacio")
    @Column(name = "equipo_id", nullable = false)
    private Long equipoId;

    @NotBlank(message = "El campo de motivo no puede ser vacio")
    @Column(nullable = false)
    private String motivo;

    @NotBlank(message = "El campo de estado no puede ser vacio")
    @Column(nullable = false)
    private String estado; // Ejemplo: "ACTIVA", "EXPIRADA", "CERRADA"

    @NotNull(message = "El campo de fecha de inicio no puede ser vacio")
    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @NotNull(message = "El campo de fecha de fin no puede ser vacio")
    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @NotBlank(message = "La severidad no puede estar vacía")
    @Column(nullable = false)
    private String severidad;

    @Embedded
    private Audit audit = new Audit();
}