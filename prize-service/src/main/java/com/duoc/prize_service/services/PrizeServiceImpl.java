package com.duoc.prize_service.services.impl;

import com.duoc.prize_service.clients.TournamentClient;
import com.duoc.prize_service.models.Prize;
import com.duoc.prize_service.models.dtos.PrizeSaveDTO;
import com.duoc.prize_service.models.dtos.TournamentDTO;
import com.duoc.prize_service.repositories.PrizeRepository;
import com.duoc.prize_service.services.PrizeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrizeServiceImpl implements PrizeService {

    private final PrizeRepository prizeRepository;
    private final TournamentClient tournamentClient; // Cliente Feign externo

    @Override
    @Transactional(readOnly = true)
    public List<Prize> findAll() {
        return prizeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Prize findById(Long id) {
        return prizeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Premio no encontrado con el ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Prize> findByTorneoId(Long torneoId) {
        return prizeRepository.findByTorneoId(torneoId);
    }

    @Override
    @Transactional

    public Prize save(PrizeSaveDTO dto) {
        log.info("Validando torneo ID: {} llamando a tournament-service via OpenFeign", dto.getTorneoId());

        TournamentDTO torneoExterno = tournamentClient.findById(dto.getTorneoId());
        log.info("Validación exitosa. Torneo encontrado: {}", torneoExterno.getNombre());

        // Mapeo limpio estructurado
        Prize prize = new Prize();
        prize.setNombre(dto.getNombre());
        prize.setMonto(dto.getMonto());
        prize.setTorneoId(dto.getTorneoId()); // ID plano desacoplado
        prize.setEstado(dto.getEstado());

        return prizeRepository.save(prize);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Prize prize = findById(id);
        prizeRepository.delete(prize);
    }
}