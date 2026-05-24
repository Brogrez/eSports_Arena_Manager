package com.duoc.team_service.services;

import com.duoc.team_service.clients.GameClient;
import com.duoc.team_service.exceptions.EquipoException;
import com.duoc.team_service.models.Equipo;
import com.duoc.team_service.models.dtos.EquipoDTO;
import com.duoc.team_service.models.dtos.GameDTO;
import com.duoc.team_service.repositories.EquipoRepository;

import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EquipoServiceImpl implements EquipoService {

    @Autowired
    private EquipoRepository equipoRepository;

    private GameClient gameClient;

    @Transactional(readOnly = true)
    @Override
    public List<EquipoDTO> findAll() {
        return equipoRepository.findAll().stream().map(e -> {
            EquipoDTO equipoDTO = new EquipoDTO();
            equipoDTO.setEquipoId(e.getEquipoId());
            equipoDTO.setNombreEquipo(e.getNombreEquipo());
            equipoDTO.setCapitanId(e.getCapitanId());
            equipoDTO.setEstado(e.getEstado());
            try{
                GameDTO juegoDTO = gameClient.findById(e.getJuegoPrincipalId());
            }catch(FeignException ex){
                throw new EquipoException(ex.getMessage());
            }
            GameDTO gameDTO = new GameDTO();
            gameDTO.setGameId(e.getJuegoPrincipalId());
            equipoDTO.setJuegoPrincipalId(gameDTO.getGameId());
            return equipoDTO;
        }).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public Equipo findByCapitanId(Long capitanId) {
        return this.equipoRepository.findByCapitanId(capitanId).orElseThrow(
                ()-> new  EquipoException("Capitan no encontrado")
        );
    }

    @Transactional(readOnly = true)
    @Override
    public Equipo findByEstado(String estado) {
        return this.equipoRepository.findByEstado(estado).orElseThrow(
                ()-> new  EquipoException("Estado no encontrado")
        );
    }
    @Transactional(readOnly = true)
    @Override
    public Equipo findByNombreEquipo(String nombreEquipo) {
        return this.equipoRepository.findByNombreEquipo(nombreEquipo).orElseThrow(
                () -> new EquipoException("Equipo no encontrado")
        );
    }

    @Override
    public List<Equipo> findByJuegoPrincipalId(Long juegoId) {
        return this.equipoRepository.findByJuegoPrincipalId(juegoId);
    }


    @Transactional(readOnly = true)
    @Override
    public Equipo findByEquipoId(Long equipoId) {
        return this.equipoRepository.findByEquipoId(equipoId).orElseThrow(
                () -> new EquipoException("Equipo no encontrado")
        );
    }

    @Transactional
    @Override
    public Equipo save(Equipo equipo) {
        if(this.equipoRepository.findByNombreEquipo(equipo.getNombreEquipo()).isPresent()){
            throw new EquipoException("Equipo existente");
        }
        return this.equipoRepository.save(equipo);
    }

    @Transactional
    @Override
    public Equipo update(Long id, Equipo equipo) {
        return this.equipoRepository.findById(id).map(e -> {
            e.setNombreEquipo(equipo.getNombreEquipo());
            e.setCapitanId(equipo.getCapitanId());
            e.setEstado(equipo.getEstado());
            e.setEquipoId(equipo.getEquipoId());
            e.setJuegoPrincipalId(equipo.getJuegoPrincipalId());
            return this.equipoRepository.save(e);
        }).orElseThrow(
                () -> new EquipoException("Equipo no encontrado")
        );
    }
    @Transactional
    @Override
    public void deletebyId(Long id) {
        this.equipoRepository.deleteById(id);
    }




}
