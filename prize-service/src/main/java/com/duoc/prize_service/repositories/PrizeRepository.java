package com.duoc.prize_service.repositories;

import com.duoc.prize_service.models.Prize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PrizeRepository extends JpaRepository<Prize, Long> {

    // Busca premios por el ID plano del torneo externo
    List<Prize> findByTorneoId(Long torneoId);
}