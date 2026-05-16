package com.duoc.tournament_service.services;

import com.duoc.tournament_service.models.Tour;

import java.time.LocalDate;
import java.util.List;

public interface TourService {
    List<Tour> findAll();
    Tour findById(Long id);
    Tour save(Tour tournament);
    Tour updateById(Tour tournament, Long id);
    void deleteById(Long id);

    List<Tour> findByGameId(Long gameId);
    List<Tour> findByEstado(String estado);
    List<Tour> findByFechaInicio(LocalDate fechaInicio);

    Tour cancelar(Long id);
    Tour cerrar(Long id);
}
