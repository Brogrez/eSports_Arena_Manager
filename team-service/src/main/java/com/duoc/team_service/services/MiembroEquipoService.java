package com.duoc.team_service.services;

import com.duoc.team_service.models.MiembroEquipo;

import java.util.List;

public interface MiembroEquipoService {
    List<MiembroEquipo> findAll();
    MiembroEquipo findByMiembroId(Long id);;
    MiembroEquipo findByUsuarioId(Long usuarioId);
    MiembroEquipo findByRolDentroEquipo(String rolDentroEquipo);
    MiembroEquipo findBymEquipoId(Long equipoId);
    MiembroEquipo save(Long usuarioId, MiembroEquipo miembroEquipo);
    void deleteByMiembroId(MiembroEquipo miembroEquipo);
    MiembroEquipo update(Long miembroEquipoId,MiembroEquipo miembroEquipo);
}
