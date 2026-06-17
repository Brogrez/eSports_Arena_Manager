package eSports_Arena_Manager.auth_service_login.config;
import eSports_Arena_Manager.auth_service_login.models.Cuenta;
import eSports_Arena_Manager.auth_service_login.models.Rol;
import eSports_Arena_Manager.auth_service_login.repositories.RolRepository;
import eSports_Arena_Manager.auth_service_login.repositories.CuentaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

// Siembra datos al arrancar: los 3 roles y 3 usuarios de prueba (uno por rol).
// Asi la demo tiene credenciales listas sin tener que registrarse a mano.
@Component
public class DataLoader implements CommandLineRunner {

    private final RolRepository rolRepository;
    private final CuentaRepository cuentaRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(RolRepository rolRepository, CuentaRepository cuentaRepository, PasswordEncoder passwordEncoder) {
        this.rolRepository = rolRepository;
        this.cuentaRepository = cuentaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        Rol admin = obtenerOCrearRol("ROLE_ADMIN");
        Rol organizador = obtenerOCrearRol("ROLE_ORGANIZADOR");
        Rol jugador = obtenerOCrearRol("ROLE_JUGADOR");

        crearUsuarioSiNoExiste("admin", "admin123", Set.of(admin));
        crearUsuarioSiNoExiste("organizador1", "organizador123", Set.of(organizador));
        crearUsuarioSiNoExiste("jugador1", "jugador123", Set.of(jugador));
    }

    private Rol obtenerOCrearRol(String nombre) {
        return this.rolRepository.findByNombre(nombre).orElseGet(() -> this.rolRepository.save(new Rol(nombre)));
    }

    private void crearUsuarioSiNoExiste(String username, String passwordPlano, Set<Rol> roles) {
        if (this.cuentaRepository.existsByNombreCuenta(username)) {
            return;
        }
        Cuenta cuenta = new Cuenta();
        cuenta.setCuentaId(cuenta.getCuentaId());
        cuenta.setPassword(this.passwordEncoder.encode(passwordPlano));
        cuenta.setRoles(roles);
        this.cuentaRepository.save(cuenta);
    }
}
