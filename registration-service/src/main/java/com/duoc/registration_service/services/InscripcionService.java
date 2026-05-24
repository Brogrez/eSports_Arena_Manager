package com.duoc.registration_service.services;

import com.duoc.registration_service.models.Inscripcion;
import com.duoc.registration_service.models.dtos.InscripcionDTO;

import java.util.List;

public interface InscripcionService {
    List<InscripcionDTO> findAll();
    Inscripcion findByInscripcionId(Long inscripcionId);
    Inscripcion findByJugadorId(Long jugadorId);
    Inscripcion findByEquipoId(Long equipoId);
    Inscripcion save(Inscripcion inscripcion);
    Inscripcion updateById(Inscripcion inscripcion);
    void deleteById(Long id);
    Inscripcion findByTorneoId (Long torneoId);
}
