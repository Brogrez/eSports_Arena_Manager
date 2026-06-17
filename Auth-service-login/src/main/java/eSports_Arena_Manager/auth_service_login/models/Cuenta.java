package eSports_Arena_Manager.auth_service_login.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

// Un usuario del sistema. Puede representar a un medico o a un paciente segun sus roles.
@Entity
@Table(name = "cuentas")
@Getter
@Setter
@NoArgsConstructor
public class Cuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cuenta_id")
    private Long cuentaId;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String nombreCuenta;

    // Se guarda CIFRADA con BCrypt, nunca en texto plano.
    @NotBlank
    @Column(nullable = false)
    private String password;

    // Relacion muchos-a-muchos: un usuario tiene varios roles y un rol lo comparten varios usuarios.
    // Se crea una tabla intermedia 'usuario_roles' con las dos llaves foraneas.
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "cuenta_roles",
            joinColumns = @JoinColumn(name = "cuenta_id"),
            inverseJoinColumns = @JoinColumn(name = "rol_id")
    )
    private Set<Rol> roles = new HashSet<>();
}
