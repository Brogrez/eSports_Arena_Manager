package com.duoc.team_service.services;

import com.duoc.team_service.exceptions.EquipoException;
import com.duoc.team_service.models.Equipo;
import com.duoc.team_service.repositories.EquipoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EquipoServiceImpl implements EquipoService {

    @Autowired
    private EquipoRepository equipoRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Equipo> findAll() {
        return equipoRepository.findAll();
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
