package com.duoc.users_service.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @NotBlank(message = "El campo de nombres no puede ser vacio")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "El campo de nickname no puede ser vacio")
    @Column(nullable = false, unique = true)
    private String nickname;

    @NotBlank(message = "El campo email no puede estar vacío")
    @Email(message = "El formato del email debe ser ejemplo@correo.com")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "El campo de rol no puede ser vacio")
    @Column(nullable = false)
    private String rol;

    @NotBlank(message = "El campo de estado no puede ser vacio")
    @Column(nullable = false)
    private String estado;

    @NotNull(message = "El campo fecha de registro no puede ser nulo")
    @Column(name = "fecha-registro")
    private LocalDateTime fechaRegistro;
}
