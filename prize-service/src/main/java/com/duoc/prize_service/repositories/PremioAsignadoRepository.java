package com.duoc.prize_service.repositories;

import com.duoc.prize_service.models.PremioAsignado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PremioAsignadoRepository extends JpaRepository<PremioAsignado, Long> {
    List<PremioAsignado> findByPremioId(Long premioId);
    boolean existsByPremioId(Long premioId);
}
