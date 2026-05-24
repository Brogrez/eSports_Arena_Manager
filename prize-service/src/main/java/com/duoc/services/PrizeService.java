package com.duoc.services;

import com.duoc.models.Prize;
import java.util.List;

public interface PrizeService {
    List<Prize> findAll();
    Prize findById(Long id);
    List<Prize> findByName(String name);
    List<Prize> findByEstado(String estado);
    List<Prize> findByTorneoId(Long torneoId);
    Prize save(Prize prize);
    void deleteById(Long id);
    Prize updateById(Long id, Prize prize);
}