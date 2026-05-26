package com.duoc.prize_service.services;

import com.duoc.prize_service.clients.TournamentClient;
import com.duoc.prize_service.clients.RankingClient;
import com.duoc.prize_service.models.Prize;
import com.duoc.prize_service.repositories.PrizeRepository;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class PrizeServiceImpl implements PrizeService {

    @Autowired
    private PrizeRepository prizeRepository;

    @Autowired
    private TournamentClient tournamentClient;

    @Autowired
    private RankingClient rankingClient;

    @Transactional(readOnly = true)
    @Override
    public List<Prize> findAll() {
        return this.prizeRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Prize findById(Long id) {
        return this.prizeRepository.findById(id).orElseThrow(
                () -> new RuntimeException("EL PREMIO NO EXISTE")
        );
    }

    @Transactional(readOnly = true)
    @Override
    public List<Prize> findByTorneoId(Long torneoId) {
        return this.prizeRepository.findByTorneoId(torneoId);
    }

    @Transactional
    @Override
    public Prize save(Prize prize) {
        try {
            this.tournamentClient.findById(prize.getTorneoId());
        } catch (FeignException e) {
            throw new RuntimeException("EL TORNEO NO EXISTE");
        }

        try {
            this.rankingClient.findByTorneoId(prize.getTorneoId());
        } catch (FeignException e) {
            throw new RuntimeException("EL RANKING DEL TORNEO NO EXISTE");
        }

        prize.setEstado("ASIGNADO");
        return this.prizeRepository.save(prize);
    }

    @Transactional
    @Override
    public Prize updateById(Long id, Prize prize) {
        return this.prizeRepository.findById(id).map(p -> {
            p.setNombre(prize.getNombre());
            p.setMonto(prize.getMonto());
            p.setTorneoId(prize.getTorneoId());
            p.setEstado(prize.getEstado());
            return this.prizeRepository.save(p);
        }).orElseThrow(
                () -> new RuntimeException("EL PREMIO NO EXISTE")
        );
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        Prize prize = this.findById(id);
        this.prizeRepository.delete(prize);
    }
}