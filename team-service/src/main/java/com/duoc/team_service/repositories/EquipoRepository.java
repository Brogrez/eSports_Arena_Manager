package com.duoc.team_service.repositories;

import com.duoc.team_service.models.Equipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EquipoRepository extends JpaRepository<Equipo,Long> {
    List<Equipo> findByJuegoPrincipalId(Long juegoId);

    Optional<Equipo> findByCapitanId(Long capitanId);

    Optional<Equipo> findByEstado(String estado);

    Optional<Equipo> findByEquipoId(Long equipoId);

    Optional<Equipo> findByNombreEquipo(String nombreEquipo);


}
