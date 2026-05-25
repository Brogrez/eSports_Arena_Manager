package com.example.ranking_service.repositories;

import com.example.ranking_service.models.Ranking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RankingRepository extends JpaRepository<Ranking, Long>{
    List<Ranking> findByTourId(Long tourId);
    Optional<Ranking> findByTourIdAndParticipanteId(Long tourId, Long participanteId);
    boolean existsByTourIdAndParticipanteId(Long tourId, Long participanteId);
    List<Ranking> findByTourIdOrderByPuntosDesc(Long tourId);
}
