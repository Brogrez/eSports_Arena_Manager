package eSports_Arena_Manager.auth_service_login.repositories;

import eSports_Arena_Manager.auth_service_login.models.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// Repositorio de roles. Hereda los metodos CRUD de JpaRepository.
@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {
    // Busca un rol por su nombre (ej: "ROLE_MEDICO"). Lo usan el registro y el seed inicial.
    Optional<Rol> findByNombre(String nombre);
}
