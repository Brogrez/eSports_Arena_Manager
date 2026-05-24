package com.duoc.registration_service.services;

import com.duoc.registration_service.clients.EquipoClient;
import com.duoc.registration_service.clients.SancionClient;
import com.duoc.registration_service.clients.TorneoClient;
import com.duoc.registration_service.clients.UsuarioClient;
import com.duoc.registration_service.exceptions.InscripcionException;
import com.duoc.registration_service.models.Inscripcion;
import com.duoc.registration_service.models.dtos.*;
import com.duoc.registration_service.repositories.InscripcionRepository;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InscripcionServiceImpl  implements InscripcionService {

    @Autowired
    InscripcionRepository inscripcionRepository;

    @Autowired
    private EquipoClient equipoClient;

    @Autowired
    private UsuarioClient usuarioClient;

    @Autowired
    private TorneoClient  torneoClient;

    @Autowired
    private SancionClient sancionClient;

    @Override
    public List<InscripcionDTO> findAll() {
        return this.inscripcionRepository.findAll().stream().map(i -> {
            InscripcionDTO dto = new InscripcionDTO();
            dto.setInscripcionId(i.getInscripcionId());
            dto.setTorneoId(i.getTorneoId());
            dto.setEquipoId(i.getEquipoId());
            dto.setUsuarioId(i.getJugadorId());
            dto.setTipoParticipante(i.getTipoParticipante());
            dto.setEstado(i.getEstado());
            dto.setFechaInscripcion(i.getFechaInscripcion());
            return dto;
        }).toList();
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

        // Validar que el torneo existe y no ha empezado
        try {
            TorneoDTO torneo = torneoClient.findById(inscripcion.getTorneoId());
            if (torneo.getFechaInicio().before(inscripcion.getFechaInscripcion())) {
                throw new InscripcionException("El torneo ya empezó, no se pueden inscribir jugadores");
            }
        } catch (FeignException e) {
            throw new InscripcionException("El torneo con id " + inscripcion.getTorneoId() + " no existe");
        }

        // Validar sanción — 404 significa sin sanción, puede inscribirse
        try {
            SancionDTO sancionDTO = sancionClient.findbyid(inscripcion.getJugadorId());
            if (sancionDTO.getEstado().equals("ACTIVA")) {
                throw new InscripcionException("El jugador tiene una sancion vigente, no puede inscribirse");
            }
        } catch (FeignException.NotFound e) {
            // 404 = no tiene sanción = puede inscribirse
        } catch (FeignException e) {
            throw new InscripcionException("Error al consultar sanciones: " + e.getMessage());
        }

        // Validar que el equipo existe
        try {
            EquipoDTO equipoDTO = this.equipoClient.findById(inscripcion.getEquipoId());
        } catch (FeignException e) {
            throw new InscripcionException("El equipo con id " + inscripcion.getEquipoId() + " no existe");
        }

        // Validar que el usuario existe
        try {
            UsuarioDTO usuarioDTO = this.usuarioClient.findById(inscripcion.getJugadorId());
        } catch (FeignException e) {
            throw new InscripcionException("El usuario con id " + inscripcion.getJugadorId() + " no existe");
        }

        inscripcion.setEstado("ACTIVO");
        return this.inscripcionRepository.save(inscripcion);
    }

    @Override
    public Inscripcion updateById(Inscripcion inscripcion, Long id) {
        return this.inscripcionRepository.findById(id).map(i -> {
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
