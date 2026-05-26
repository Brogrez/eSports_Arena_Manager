package com.duoc.prize_service.services;

import com.duoc.prize_service.models.PremioAsignado;
import com.duoc.prize_service.models.Prize;
import java.util.List;

public interface PrizeService {
    List<Prize> findAll();
    Prize findById(Long id);
    Prize save(Prize prize);
    Prize updateById(Long id, Prize prize);
    void deleteById(Long id);
    List<Prize> findByTorneoId(Long torneoId);
    List<Prize> findByPosicion(Integer posicion);
    PremioAsignado asignarPremio(Long premioId, Long participanteId);
}