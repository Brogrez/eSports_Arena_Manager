package eSports_Arena_Manager.auth_service.repositories;

import eSports_Arena_Manager.auth_service.models.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, Long> {
    Optional<Cuenta> findByUsuario(String usuario);

    Optional<Cuenta> findByCorreo(String correo);

}
