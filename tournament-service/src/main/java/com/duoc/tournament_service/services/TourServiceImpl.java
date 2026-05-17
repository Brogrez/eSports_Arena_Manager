package com.duoc.tournament_service.services;

import com.duoc.tournament_service.clients.GameClient;
import com.duoc.tournament_service.exceptions.TourException;
import com.duoc.tournament_service.models.Tour;
import com.duoc.tournament_service.models.dtos.GameDto;
import com.duoc.tournament_service.repositories.TourRepository;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class TourServiceImpl implements TourService {

    @Autowired
    private TourRepository tourRepository;

    @Autowired
    private GameClient gameClient;

    @Transactional(readOnly = true)
    @Override
    public List<Tour> findAll() {
        return this.tourRepository.findAll();
    }

    @Override
    public Tour findById(Long id) {
        return this.tourRepository.findById(id).orElseThrow(
                () -> new TourException("Torneo no encontrado")
        );
    }

    @Transactional
    @Override
    public Tour save(Tour tournament) {
        try{
            GameDto game = this.gameClient.findByid(tournament.getGameId());
            if(game.getEstado().equals("INACTIVO")){
                throw new TourException("el juego esta inactivo");
            }
        }catch (FeignException e){
            throw new TourException("el juego no existe");
        }

        if(tournament.getFechaInicio().isBefore(LocalDate.now())){
            throw new TourException("la fecha de inicio debe ser posterior a la fecha actual");
        }
        if(tournament.getFechaFin().isBefore(tournament.getFechaInicio())){
            throw new TourException("la fecha fin debe ser posterior a la fecha de inicio");
        }

        tournament.setEstado("BORRADOR");
        return this.tourRepository.save(tournament);
    }

    @Transactional
    @Override
    public Tour updateById(Long id, Tour tournament) {
        return this.tourRepository.findById(id).map(t -> {
            if(t.getEstado().equals("EN_CURSO")){
                throw new TourException("no se puede modificar torneos en curso");
            }

            t.setFechaInicio(tournament.getFechaInicio());
            t.setFechaFin(tournament.getFechaFin());
            t.setCupoMaximo(tournament.getCupoMaximo());
            t.setEstado(tournament.getEstado());

            return this.tourRepository.save(t);
        }).orElseThrow(
                () -> new TourException("el torneo no existe")
        );
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        this.tourRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Tour> findByGameId(Long gameId) {
        return this.tourRepository.findByGameId(gameId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Tour> findByEstado(String estado) {
        return this.tourRepository.findByEstado(estado);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Tour> findByFechaInicio(LocalDate fechaInicio) {
        return this.tourRepository.findByFechaInicio(fechaInicio);
    }

    @Transactional
    @Override
    public Tour cancelar(Long id) {
        Tour tournament = this.tourRepository.findById(id).orElseThrow(
                () -> new TourException("el torneo no existe")
        );

        if(tournament.getEstado().equals("FINALIZADO") || tournament.getEstado().equals("CANCELADO")){
            throw new TourException("el torneo no se puede cancelar");
        }

        tournament.setEstado("CANCELADO");
        return this.tourRepository.save(tournament);
    }

    @Transactional
    @Override
    public Tour cerrar(Long id) {
        Tour tournament = this.tourRepository.findById(id).orElseThrow(
                () -> new TourException("el torneo no existe")
        );

        if(!tournament.getEstado().equals("EN_CURSO")){
            throw new TourException("solo se pueden cerrar torneos que esten en curso");
        }
        tournament.setEstado("FINALIZADO");
        return this.tourRepository.save(tournament);
    }
}
