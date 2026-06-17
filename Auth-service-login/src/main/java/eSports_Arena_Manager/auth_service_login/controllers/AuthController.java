package eSports_Arena_Manager.auth_service_login.controllers;

import eSports_Arena_Manager.auth_service_login.dtos.AuthResponse;
import eSports_Arena_Manager.auth_service_login.dtos.LoginRequest;
import eSports_Arena_Manager.auth_service_login.dtos.RegisterRequest;
import eSports_Arena_Manager.auth_service_login.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Endpoints PUBLICOS (no requieren token): registrarse y autenticarse.
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Autenticacion", description = "Registro y login. Devuelve el JWT que usa todo el sistema.")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @Operation(summary = "Registrar usuario", description = "Crea un usuario y devuelve su token. Roles validos: ROLE_ADMIN, ROLE_ORGANIZADOR, ROLE_JUGADOR")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.authService.register(request));
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesion", description = "Valida usuario y clave, y devuelve el token JWT")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(this.authService.login(request));
    }
}
