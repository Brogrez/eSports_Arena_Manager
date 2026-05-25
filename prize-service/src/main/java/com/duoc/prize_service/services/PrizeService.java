package com.duoc.prize_service.services;

import com.duoc.prize_service.models.Prize;
import com.duoc.prize_service.models.dtos.PrizeSaveDTO;
import java.util.List;

public interface PrizeService {
    List<PrizeSaveDTO> findAll();
    Prize findById(Long id);
    List<Prize> findByTorneoId(Long torneoId);
    Prize save(Prize prize);
    void deleteById(Long id);
    Prize update(Long id, PrizeSaveDTO prizeSaveDTO);
}