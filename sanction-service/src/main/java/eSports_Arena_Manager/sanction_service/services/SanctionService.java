package eSports_Arena_Manager.sanction_service.services;

import eSports_Arena_Manager.sanction_service.models.Sanction;
import java.util.List;

public interface SanctionService {
    List<Sanction> findAll();
    Sanction findById(Long id);
    Sanction save(Sanction sanction);
    void deleteById(Long id);
    Sanction updateById(Long id, Sanction sanction);
    List<Sanction> findByUsuarioId(Long usuarioId);
    List<Sanction> findByTeamId(Long teamId);
    List<Sanction> findByEstado(String estado);
    Sanction cerrar(Long id);
}

