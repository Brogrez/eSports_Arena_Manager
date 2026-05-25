package com.duoc.prize_service.services;

import com.duoc.prize_service.models.Prize;
import com.duoc.prize_service.models.dtos.PrizeSaveDTO;
import java.util.List;

public interface PrizeService {
    List<Prize> findAll();
    Prize findById(Long id);
    List<Prize> findByTorneoId(Long torneoId);
    Prize save(PrizeSaveDTO prizeSaveDTO);
    void delete(Long id);
}