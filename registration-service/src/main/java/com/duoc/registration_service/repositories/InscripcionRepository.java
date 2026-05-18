package com.duoc.registration_service.repositories;

import com.duoc.registration_service.models.Inscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {
    Optional<Inscripcion> findByTorneoId(Long torneoId);
    Optional<Inscripcion> findByEquipoId(Long equipoId);
    Optional<Inscripcion> findByJugadorId(Long jugadorId);
    Optional<Inscripcion> findByTorneoIdAndJugadorId(Long torneoId, Long jugadorId); //para  evitar duplicados
}
