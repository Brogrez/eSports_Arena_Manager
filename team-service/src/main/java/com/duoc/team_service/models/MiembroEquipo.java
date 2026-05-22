package com.duoc.team_service.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="miembro_equipo")
@ToString
@Getter
@Setter
@NoArgsConstructor
public class MiembroEquipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long miembroId;

    @NotNull(message = "Debe tener el id correspondiente")
    @Column(nullable = false)
    private Long mEquipoId;

    @NotNull(message = "debe tener el id del miembro de equipo")
    @Column(nullable = false)
    private Long usuarioId;

    @NotBlank(message = "el miembro debe tener un rol")
    @Column(nullable = false)
    private String rolDentroEquipo;

    @Embedded
    private Audit audit = new Audit();

}
