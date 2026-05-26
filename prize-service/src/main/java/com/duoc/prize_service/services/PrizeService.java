package com.duoc.prize_service.services;

import com.duoc.prize_service.models.Prize;
import java.util.List;

public interface PrizeService {
    List<Prize> findAll();
    Prize findById(Long id);
    Prize save(Prize prize);
    void deleteById(Long id);
    Prize updateById(Long id, Prize prize);
    List<Prize> findByTorneoId(Long torneoId);
}