package eSports_Arena_Manager.auth_service_login.repositories;

import eSports_Arena_Manager.auth_service_login.models.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// Repositorio de usuarios. Al extender JpaRepository ya hereda save, findById, findAll, etc.
// Los metodos de abajo son "consultas derivadas": Spring Data genera el SQL leyendo el nombre del metodo.
@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, Long> {
    // findBy + Username  ->  SELECT * FROM usuarios WHERE username = ?  (lo usa el login)
    Optional<Cuenta> findByNombreCuenta(String nombreCuenta);
    // existsBy + Username  ->  devuelve true/false (lo usa el registro para evitar duplicados)
    boolean existsByNombreCuenta(String nombreCuenta);
}
