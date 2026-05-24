package com.duoc.registration_service.services;

import com.duoc.registration_service.clients.EquipoClient;
import com.duoc.registration_service.clients.SancionClient;
import com.duoc.registration_service.clients.TorneoClient;
import com.duoc.registration_service.clients.UsuarioClient;
import com.duoc.registration_service.exceptions.InscripcionException;
import com.duoc.registration_service.models.Inscripcion;
import com.duoc.registration_service.models.dtos.EquipoDTO;
import com.duoc.registration_service.models.dtos.InscripcionDTO;
import com.duoc.registration_service.models.dtos.SancionDTO;
import com.duoc.registration_service.models.dtos.UsuarioDTO;
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

    private EquipoClient equipoClient;

    private UsuarioClient usuarioClient;

    private TorneoClient  torneoClient;

    private SancionClient sancionClient;

    @Override
    public List<InscripcionDTO> findAll() {
        return this.inscripcionRepository.findAll().stream().map(i->{
            InscripcionDTO inscripcionDTO = new InscripcionDTO();
            inscripcionDTO.setInscripcionId(i.getInscripcionId());
            inscripcionDTO.setTipoParticipante(i.getTipoParticipante());
            inscripcionDTO.setEstado(i.getEstado());
            SancionDTO sancionDTO = null;
            if(i.getFechaInscripcion().before(torneoClient.findById(i.getTorneoId()).getFechaInicio())){
                inscripcionDTO.setFechaInscripcion(i.getFechaInscripcion());
            }else {
                throw new InscripcionException("el torneo ya empezó, no se pueden inscribir jugadores");
            }
            try{
                sancionDTO = sancionClient.findbyid(i.getJugadorId());
                if(sancionDTO == null){
                    inscripcionDTO.setUsuarioId(i.getJugadorId());
                }else{
                    throw new InscripcionException("El jugador tiene una sancion vigente, no se puede inscribir");
                }
            }catch(InscripcionException e){
                e.getMessage();
            }
            inscripcionDTO.setEquipoId(i.getEquipoId());
            inscripcionDTO.setTorneoId(i.getTorneoId());
            return inscripcionDTO;
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
       try {
              if (torneoClient.findById(inscripcion.getTorneoId()).getFechaInicio().before(inscripcion.getFechaInscripcion())) {
                throw new InscripcionException("El torneo ya empezó, no se pueden inscribir jugadores");
              }
              SancionDTO sancionDTO = sancionClient.findbyid(inscripcion.getJugadorId());
              if (sancionDTO != null) {
                throw new InscripcionException("El jugador tiene una sancion vigente, no se puede inscribir");
              }
       }catch(FeignException e){
              throw new InscripcionException(e.getMessage());

        }

       try{
           EquipoDTO equipoDTO = this.equipoClient.findById(inscripcion.getEquipoId());
         }catch (FeignException e){
           throw new InscripcionException("El equipo con id "+ inscripcion.getEquipoId() +" no existe");
       }
       try{
           UsuarioDTO usuarioDTO = this.usuarioClient.findById(inscripcion.getJugadorId());
       }catch (FeignException e){
           throw new InscripcionException("El usuario con id "+ inscripcion.getJugadorId() +" no existe");
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
