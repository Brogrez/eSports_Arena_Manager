package com.duoc.tournament_service.repositories;

import com.duoc.tournament_service.models.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TourRepository extends JpaRepository<Tour, Long> {

    List<Tour> findByGameId(Long gameId);

    List<Tour> findByEstado(String estado);

    List<Tour> findByFechaInicio(LocalDate fechaInicio);

}
