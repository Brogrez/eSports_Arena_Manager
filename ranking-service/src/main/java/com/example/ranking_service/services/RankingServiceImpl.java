package com.example.ranking_service.services;


import com.example.ranking_service.clients.ResultClient;
import com.example.ranking_service.clients.TourClient;
import com.example.ranking_service.exceptions.RankingExceptions;
import com.example.ranking_service.models.Ranking;
import com.example.ranking_service.models.dtos.TourDto;
import com.example.ranking_service.repositories.RankingRepository;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RankingServiceImpl implements RankingService{

    @Autowired
    private RankingRepository rankingRepository;

    @Autowired
    private TourClient tourClient;

    @Autowired
    private ResultClient resultClient;

    @Transactional(readOnly = true)
    @Override
    public List<Ranking> findAll() {
        return this.rankingRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Ranking findById(Long id) {
        return this.rankingRepository.findById(id).orElseThrow(
                () -> new RankingExceptions("ranking no existe")
        );
    }

    @Transactional
    @Override
    public Ranking save(Ranking ranking) {
        try{
            TourDto tourDto = tourClient.findById(ranking.getTourId());
        }catch(FeignException e){
            throw new RankingExceptions("el torneo no existe");
        }

        if(rankingRepository.existsByTourIdAndParticipanteId(ranking.getTourId(), ranking.getParticipanteId())){
            throw new RankingExceptions("el participante ya tiene un registro en el ranking");
        }
        ranking.setPuntos(0);
        ranking.setVictorias(0);
        ranking.setDerrotas(0);
        ranking.setDiferencia(0);
        ranking.setPosicion(0);

        return this.rankingRepository.save(ranking);
    }

    @Transactional
    @Override
    public Ranking updateById(Ranking ranking, Long id) {
        return this.rankingRepository.findById(id).map(r ->{
            r.setPuntos(ranking.getPuntos());
            r.setVictorias(ranking.getVictorias());
            r.setDerrotas(ranking.getDerrotas());
            r.setDiferencia(ranking.getDiferencia());
            r.setPosicion(ranking.getPosicion());
            return this.rankingRepository.save(r);
        }).orElseThrow(
                () -> new RankingExceptions("el ranking no existe")
        );
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        this.rankingRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Ranking> findByTourId(Long tourId) {
        return this.rankingRepository.findByTourIdOrderByPuntosDesc(tourId);
    }

    @Transactional(readOnly = true)
    @Override
    public Ranking findByTourIdAndParticipanteId(Long tourId, Long participanteId) {
        return this.rankingRepository.findByTourIdAndParticipanteId(tourId, participanteId).orElseThrow(
                () -> new RankingExceptions("no existe ranking para el participante en el torneo")
        );
    }

    @Transactional
    @Override
    public Ranking actualizarPuntos(Long id, Integer puntos, Integer victorias, Integer derrotas) {
        Ranking ranking = this.rankingRepository.findById(id).orElseThrow(
                () -> new RankingExceptions("el rankin no existe")
        );
        ranking.setPuntos(ranking.getPuntos() + puntos);
        ranking.setVictorias(ranking.getVictorias() + victorias);
        ranking.setDerrotas(ranking.getDerrotas() + derrotas);
        ranking.setDiferencia(ranking.getDiferencia() + (victorias - derrotas));
        recalcularPosiciones(ranking.getTourId());

        return this.rankingRepository.save(ranking);

    }

    @Transactional
    @Override
    public void cerrarRanking(Long tourId) {
        List<Ranking> rankings = this.rankingRepository.findByTourId(tourId);
        if (rankings.isEmpty()) {
            throw new RankingExceptions("No existe ranking para el torneo con id " + tourId);
        }
        // Al cerrar el torneo las posiciones quedan fijas
        recalcularPosiciones(tourId);
    }

    private void recalcularPosiciones(Long tourId) {
        List<Ranking> rankings = this.rankingRepository.findByTourIdOrderByPuntosDesc(tourId);
        for (int i = 0; i < rankings.size(); i++) {
            rankings.get(i).setPosicion(i + 1);
            this.rankingRepository.save(rankings.get(i));
        }
    }
}
