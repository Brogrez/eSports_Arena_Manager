package com.duoc.prize_service.repositories; // Alineado a tu ruta física real actual

import com.duoc.models.Prize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrizeRepository extends JpaRepository<Prize, Long> {

    Optional<Prize> findByName(String name);

    List<Prize> findByEstado(String estado);

    List<Prize> findByTorneoId(Long torneoId);

    boolean existsByName(String name);
}