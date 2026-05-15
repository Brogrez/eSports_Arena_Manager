package com.duoc.team_service.services;

import com.duoc.team_service.models.Equipo;

import java.util.List;
import java.util.Optional;

public interface EquipoService {
    List<Equipo> findAll();
    Equipo findByCapitanId(Long capitanId);
    Equipo findByEstado(String estado);
    Equipo findByEquipoId(Long equipoId);
    Equipo save(Equipo equipo);
    Equipo update(Long id,Equipo equipo);
    void deletebyId(Long id);
    Equipo findByNombreEquipo(String nombreEquipo);
}
