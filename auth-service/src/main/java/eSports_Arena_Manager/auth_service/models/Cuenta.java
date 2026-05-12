package eSports_Arena_Manager.auth_service.models;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;


@Entity
@Table(name = "cuentas")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Cuenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="cuenta_id")
    private Long cuentaId;

    @NotBlank(message = "El campo de correo no puede ser vacio")
    @Email(message = "El campo de correo tiene que tener el formato de correo")
    @Column(nullable = false, unique = true)
    private String correo;

    @NotBlank(message = "PasswordHash no puede estar en blanco")
    @Column(nullable = false)
    private String passwordHash;

    @NotBlank(message = "tiene que haber un rol asignado")
    @Column(nullable = false)
    private String rol;

    @NotBlank(message = "tiene que haber un estado asignado")
    @Column(nullable = false)
    private String estado;

    @NotNull(message = "El campo de fecha de creacion no puede ser vacio")
    @Column(nullable = false)
    private LocalDateTime fechaCreacion;


    @Embedded
    private Audit audit = new Audit();




}
