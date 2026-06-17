package eSports_Arena_Manager.auth_service_login.services;

import eSports_Arena_Manager.auth_service_login.dtos.AuthResponse;
import eSports_Arena_Manager.auth_service_login.dtos.LoginRequest;
import eSports_Arena_Manager.auth_service_login.dtos.RegisterRequest;
import eSports_Arena_Manager.auth_service_login.models.Rol;
import eSports_Arena_Manager.auth_service_login.models.Cuenta;
import eSports_Arena_Manager.auth_service_login.repositories.RolRepository;
import eSports_Arena_Manager.auth_service_login.repositories.CuentaRepository;
import eSports_Arena_Manager.auth_service_login.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

// Logica de autenticacion: crear usuarios (register) y validar credenciales (login).
// En ambos casos termina entregando un JWT firmado para que el cliente lo use en las demas peticiones.
@Service
public class AuthService {

    // Dependencias inyectadas por el constructor (la forma recomendada en Spring):
    private final CuentaRepository cuentaRepository; // acceso a la tabla de usuarios
    private final RolRepository rolRepository;         // acceso a la tabla de roles
    private final PasswordEncoder passwordEncoder;     // cifra y compara contrasenas (BCrypt)
    private final JwtService jwtService;               // genera el token firmado

    public AuthService(CuentaRepository cuentaRepository, RolRepository rolRepository,
                       PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.cuentaRepository = cuentaRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    // REGISTRO: crea un usuario nuevo y le devuelve un token (queda logueado de inmediato).
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // 1) No permitir usernames repetidos.
        if (this.cuentaRepository.existsByNombreCuenta(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El usuario ya existe");
        }

        // 2) Si el cliente no indico roles, se asigna PACIENTE por defecto.
        Set<String> nombresRoles = (request.getRoles() == null || request.getRoles().isEmpty())
                ? Set.of("ROLE_JUGADOR")
                : request.getRoles();

        // 3) Buscar cada rol en la BD. Si piden un rol que no existe, se rechaza (400).
        Set<Rol> roles = nombresRoles.stream()
                .map(nombre -> this.rolRepository.findByNombre(nombre).orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rol no existe: " + nombre)))
                .collect(Collectors.toCollection(HashSet::new));

        // 4) Armar el usuario. La contrasena se guarda CIFRADA con BCrypt, nunca en texto plano.
        Cuenta cuenta = new Cuenta();
        cuenta.setNombreCuenta(request.getUsername());
        cuenta.setPassword(this.passwordEncoder.encode(request.getPassword()));
        cuenta.setRoles(roles);
        this.cuentaRepository.save(cuenta);

        // 5) Devolver el token ya firmado con sus roles dentro.
        return construirRespuesta(cuenta);
    }

    // LOGIN: valida usuario y clave. Si todo cuadra, entrega el token.
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        // Buscar el usuario. Si no existe, se responde el MISMO error que clave mala,
        // para no revelar si el problema fue el usuario o la contrasena.
        Cuenta cuenta = this.cuentaRepository.findByNombreCuenta(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales invalidas"));

        // Comparar la clave enviada contra el hash BCrypt guardado. matches() vuelve a cifrar y compara.
        if (!this.passwordEncoder.matches(request.getPassword(), cuenta.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales invalidas");
        }

        return construirRespuesta(cuenta);
    }

    // Genera el token y arma la respuesta (token + datos basicos del usuario, sin la contrasena).
    private AuthResponse construirRespuesta(Cuenta cuenta) {
        String token = this.jwtService.generarToken(cuenta);
        Set<String> roles = cuenta.getRoles().stream().map(Rol::getNombre).collect(Collectors.toSet());
        return new AuthResponse(token, "Bearer", cuenta.getNombreCuenta(), roles);
    }
}
