package com.duoc.prize_service.repositories;

import com.duoc.prize_service.models.Prize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PrizeRepository extends JpaRepository<Prize, Long> {

    List<Prize> findByTorneoId(Long torneoId);
    List<Prize> findByPosicion(Integer posicion);
    boolean existsByTorneoIdAndPosicion(Long torneoId, Integer posicion);


}