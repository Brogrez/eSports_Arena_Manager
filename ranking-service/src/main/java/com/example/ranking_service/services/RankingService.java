package com.example.ranking_service.services;

import com.example.ranking_service.models.Ranking;

import java.util.List;

public interface RankingService {
    List<Ranking> findAll();
    Ranking findById(Long id);
    Ranking save(Ranking ranking);
    Ranking updateById(Ranking ranking, Long id);
    void deleteById(Long id);
    List<Ranking> findByTourId(Long tourId);
    Ranking findByTourIdAndParticipanteId(Long TourId, Long participanteId);
    Ranking actualizarPuntos(Long id, Integer puntos, Integer victorias, Integer derrotas);
    void cerrarRanking(Long tourId);
}
