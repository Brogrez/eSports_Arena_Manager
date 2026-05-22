package com.duoc.team_service.repositories;

import com.duoc.team_service.models.MiembroEquipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MiembroEquipoRepository extends JpaRepository<MiembroEquipo,Long> {

    Optional<MiembroEquipo> findByMiembroId(Long miembroId);
    Optional<MiembroEquipo> findBymEquipoId(Long equipoId);
    Optional<MiembroEquipo> findByUsuarioId(Long usuarioId);
    Optional<MiembroEquipo> findByRolDentroEquipo(String rolDentroEquipo);

}
