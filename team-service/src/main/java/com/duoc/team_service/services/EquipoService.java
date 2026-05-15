package com.duoc.team_service.services;

import com.duoc.team_service.models.Equipo;

import java.util.List;

public interface EquipoService {
    List<Equipo> findAll();
    List<Equipo> findByCapitanId(Long capitanId);
    List<Equipo> findByEstado(String estado);
    Equipo save(Equipo equipo);
    Equipo update(Equipo equipo);
    void delete(Equipo equipo);
}
