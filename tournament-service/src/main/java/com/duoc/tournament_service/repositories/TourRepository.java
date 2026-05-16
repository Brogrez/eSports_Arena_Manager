package com.duoc.tournament_service.repositories;

import com.duoc.tournament_service.models.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface TourRepository extends JpaRepository<Tour, Long> {

    Optional<Tour> findByGameId(Long gameId);

    Optional<Tour> findByEstado(String estado);

    Optional<Tour> findByFechaInicio(LocalDate fechaInicio);

}
