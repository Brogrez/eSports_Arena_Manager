package com.example.match_service.services;

import com.example.match_service.models.Match;

import java.util.List;

public interface MatchService {
    List<Match> findAll();
    Match findById(Long id);
    Match save(Match match);
    Match updateById(Match match, Long id);
    void deleteById(Long id);
    List<Match> findByTorneoId(Long torneoId);
    List<Match> findByEstado(String estado);
    List<Match> findByRonda(String ronda);
    Match cancelar(Long id);
}
