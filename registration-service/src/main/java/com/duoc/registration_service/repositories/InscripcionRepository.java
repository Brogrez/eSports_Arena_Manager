package com.duoc.registration_service.repositories;

import com.duoc.registration_service.models.Inscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {
    List<Inscripcion> findByTorneoId(Long torneoId);
    List<Inscripcion> findByEquipoId(Long equipoId);
    List<Inscripcion> findByJugadorId(Long jugadorId);
    Optional<Inscripcion> findByTorneoIdAndJugadorId(Long torneoId, Long jugadorId); //para  evitar duplicados
}
