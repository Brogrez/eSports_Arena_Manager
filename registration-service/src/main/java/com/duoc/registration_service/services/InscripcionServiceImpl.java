package com.duoc.registration_service.services;

import com.duoc.registration_service.exceptions.InscripcionException;
import com.duoc.registration_service.models.Inscripcion;
import com.duoc.registration_service.repositories.InscripcionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InscripcionServiceImpl  implements InscripcionService {

    @Autowired
    InscripcionRepository inscripcionRepository;


    @Override
    public List<Inscripcion> findAll() {
        return this.inscripcionRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Inscripcion findByInscripcionId(Long inscripcionId) {
        return this.inscripcionRepository.findById(inscripcionId).orElseThrow(
                () -> new InscripcionException("Inscripcion no encontrada")
        );
    }

    @Transactional(readOnly = true)
    @Override
    public Inscripcion findByJugadorId(Long jugadorId) {
        return this.inscripcionRepository.findByJugadorId(jugadorId).orElseThrow(
                () -> new InscripcionException("jugador no encontrado")
        );
    }

    @Transactional(readOnly = true)
    @Override
    public Inscripcion findByEquipoId(Long equipoId) {
        return this.inscripcionRepository.findByEquipoId(equipoId).orElseThrow(
                () -> new InscripcionException("Equipo no encontrado")
        );
    }

    @Transactional
    @Override
    public Inscripcion save(Inscripcion inscripcion) {
        if (this.findByJugadorId(inscripcion.getJugadorId()) != null) {
            throw new InscripcionException("Jugador ya inscrito");
        }
        if (this.findByEquipoId(inscripcion.getEquipoId()) != null) {
            throw new InscripcionException("Equipo ya inscrito");
        }
        return this.inscripcionRepository.save(inscripcion);
    }

    @Override
    public Inscripcion updateById(Inscripcion inscripcion) {
        return this.inscripcionRepository.findById(inscripcion.getInscripcionId()).map(i -> {
            i.setJugadorId(inscripcion.getJugadorId());
            i.setEquipoId(inscripcion.getEquipoId());
            i.setInscripcionId(inscripcion.getInscripcionId());
            i.setEstado(inscripcion.getEstado());
            i.setFechaInscripcion(inscripcion.getFechaInscripcion());
            i.setTipoParticipante(inscripcion.getTipoParticipante());
            i.setTorneoId(inscripcion.getTorneoId());
            return this.inscripcionRepository.save(i);
        }).orElseThrow(
                () -> new InscripcionException("Inscripcion no encontrada")
        );
    }
    @Override
    public void deleteById(Long id) {
        this.inscripcionRepository.deleteById(id);
    }

    @Override
    public Inscripcion findByTorneoId(Long torneoId) {
        return this.inscripcionRepository.findByTorneoId(torneoId).orElseThrow(
                () -> new InscripcionException("Torneo no encontrado")
        );
    }
}
