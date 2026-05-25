package com.example.match_service.repositories;

import com.example.match_service.models.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
    List<Match> findByTourId(Long tourId);
    List<Match> findByEstado(String estado);
    List<Match> findByRound(String round);
    boolean existsByParticipanteAIdAndParticipanteBIdAndRound(Long participanteAId, Long participanteBId, String round);
}